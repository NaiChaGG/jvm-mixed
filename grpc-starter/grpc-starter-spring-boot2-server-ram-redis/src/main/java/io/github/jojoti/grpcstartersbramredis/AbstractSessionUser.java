package io.github.jojoti.grpcstartersbramredis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import io.github.jojoti.grpcstartersbram.Session;
import io.github.jojoti.grpcstartersbram.SessionNotCreatedException;
import io.github.jojoti.grpcstartersbram.SessionUser;
import io.github.jojoti.utilhashidtoken.HashIdToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Map;

/**
 * @author JoJo Wang
 * @link github.com/jojoti
 */
final class AbstractSessionUser implements SessionUser {

    private static final Logger log = LoggerFactory.getLogger(AbstractSessionUser.class);

    private final TokenDAO tokenDAO;
    private final ObjectMapper objectMapper;
    // 更新时使用 影子实例来更新属性，避开多线程锁的问题
    private volatile InlineEntity entity;

    protected AbstractSessionUser(TokenDAO tokenDAO, ObjectMapper objectMapper, Session.ParseToken token, ImmutableList<String> attachInline) {
        this.tokenDAO = tokenDAO;
        this.objectMapper = objectMapper;
        this.validToken(token, attachInline);
    }

    protected AbstractSessionUser(TokenDAO tokenDAO, ObjectMapper objectMapper, long uid, long scopeId, ImmutableList<String> attachInline) {
        this.tokenDAO = tokenDAO;
        this.objectMapper = objectMapper;
        final var session = tokenDAO.getSession(uid, scopeId, attachInline);
        if (session.size() == 0) {
            this.newAnonymous();
        } else {
            // 查询到了，表示会话存在
            this.entity = new InlineEntity(uid, scopeId, session);
        }
    }

    private void validToken(Session.ParseToken token, ImmutableList<String> attachInline) {
        if (token.getDecodeToken() == null) {
            this.newAnonymous();
            return;
        }

        final var tokenParse = token.getDecodeToken();

        final var uid = tokenParse.uid;
        // 约定 写入的 scopeId 为 int 参考 session interface
        final var scopeId = (int) tokenParse.scopeId;

        // fixme 可以针对 scopeId 对 ATTACH_TTL_KEY 做缓存，减少 redis 查询出来的数据
        // 后续需要再优化
        final var hashKeys = ImmutableList.<String>builder()
                .add(ATTACH_SLAT_KEY)
                .add(ATTACH_TTL_KEY)
                .addAll(attachInline);

        // 只获取这次需要一次查询的，否则使用延迟查询
        final var hashValues = this.tokenDAO.getSession(uid, scopeId, hashKeys.build());

        // 至少要存在 slat
        if (hashValues.size() < 2) {
            this.newAnonymous();
            this.tokenDAO.logoutAsync(uid, scopeId);
            log.error("redis data error 1, check read & write");
            return;
        }

        final var foundSlat = hashValues.remove(ATTACH_SLAT_KEY);

        if (Strings.isNullOrEmpty(foundSlat) || !foundSlat.equals(tokenParse.salt)) {
            this.newAnonymous();
            this.tokenDAO.logoutAsync(uid, scopeId);
            log.error("redis data error 2, check read & write");
            return;
        }

        final var foundTtl = hashValues.remove(ATTACH_TTL_KEY);
        if (Strings.isNullOrEmpty(foundTtl)) {
            this.newAnonymous();
            this.tokenDAO.logoutAsync(uid, scopeId);
            log.error("redis data error 3, check read & write");
            return;
        }
        Duration ttl;
        try {
            ttl = Duration.ofMillis(Long.parseLong(foundTtl));
        } catch (Exception e) {
            this.newAnonymous();
            this.tokenDAO.logoutAsync(uid, scopeId);
            log.error("redis data error 4, check read & write", e);
            return;
        }

        // 异步延长 token
        this.tokenDAO.expireTokenAsync(uid, scopeId, ttl);

        this.entity = new InlineEntity(uid, scopeId, hashValues).setTtl(ttl);
    }

    private void newAnonymous() {
        this.entity = new InlineEntity(0, 0, Map.of());
    }

    private void checkSession(InlineEntity inlineEntity) {
        if (inlineEntity.uid <= 0) {
            throw SessionNotCreatedException.newUnauthenticated("uid not found");
        }
    }

    @Override
    public long getScopeId() {
        final var entityRef = this.entity;
        this.checkSession(entityRef);
        return entityRef.scopeId;
    }

    @Override
    public long getUid() {
        final var entityRef = this.entity;
        this.checkSession(entityRef);
        return entityRef.uid;
    }

    @Override
    public long getUidAsDefault() {
        final var entityRef = this.entity;
        return entityRef.uid;
    }

    @Override
    public boolean isAnonymous() {
        final var entityRef = this.entity;
        return entityRef.uid <= 0;
    }

    @Override
    public void logout() {
        final var entityRef = this.entity;
        this.checkSession(entityRef);
        this.tokenDAO.logoutSync(entityRef.uid, entityRef.scopeId);
    }

    @Override
    public NewTokenBuilder newToken(long uid, long scopeId) {
        final var newToken = HashIdToken.createToken(uid, scopeId);

        final var hashValues = Maps.<String, String>newHashMap();
        hashValues.put(ATTACH_SLAT_KEY, newToken.getSlat());
        final var newInline = new InlineEntity(uid, scopeId, hashValues);

        return new NewTokenBuilder() {

            @Override
            public NewTokenBuilder setAttachString(String key, String val) {
                newInline.attach.put(key, val);
                return this;
            }

            @Override
            public NewTokenBuilder setTtl(Duration ttl) {
                newInline.ttl = ttl;
                return this;
            }

            @Override
            public <T> NewTokenBuilder setAttachJson(String key, T t) {
                try {
                    newInline.attach.put(key, objectMapper.writeValueAsString(t));
                    // 这里 不做字符串缓存，因为写了还要读出来是低概率
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
                return this;
            }

            @Override
            public String build() {
                newInline.attach.put(ATTACH_TTL_KEY, String.valueOf(newInline.ttl.toMillis()));
                // 保存 session 到数据库 里
                tokenDAO.addAttachSync(uid, scopeId, newInline.ttl, newInline.attach);
                // 影子 实例更新
                AbstractSessionUser.this.entity = newInline;
                return newToken.getTokenBase64();
            }

        };
    }

    @Override
    public String getAttachString(String key) {
        final var entityRef = this.entity;
        return entityRef.attach.get(key);
    }

    @Override
    public <T> T getAttachJson(String key, Class<T> t) {
        final var entityRef = this.entity;
        final var found = entityRef.cached.get(key);
        if (found != null) {
            return (T) found;
        }

        final var foundStr = entityRef.attach.get(key);
        if (Strings.isNullOrEmpty(foundStr)) {
            return null;
        }

        try {
            final var foundT = this.objectMapper.readValue(foundStr, t);
            entityRef.cached.put(key, foundT);
            return foundT;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public SessionUser setAttach(ImmutableMap<String, String> stringValues) {
        final var entityRef = this.entity;
        this.checkSession(entityRef);
        stringValues.forEach((K, V) -> {
            entityRef.attach.put(SessionUser.checkSetAttachKey(K), V);
        });
        tokenDAO.addAttachAsync(entityRef.uid, entityRef.scopeId, entityRef.ttl, stringValues);
        return this;
    }

    @Override
    public <T> SessionUser setAttachJson(ImmutableMap<String, T> jsonValues) {
        final var entityRef = this.entity;
        this.checkSession(entityRef);
        final var strings = Maps.<String, String>newHashMap();
        for (Map.Entry<String, T> stringTEntry : jsonValues.entrySet()) {
            try {
                entityRef.cached.put(SessionUser.checkSetAttachKey(stringTEntry.getKey()), stringTEntry.getValue());
                strings.put(stringTEntry.getKey(), this.objectMapper.writeValueAsString(stringTEntry.getValue()));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
        tokenDAO.addAttachAsync(entityRef.uid, entityRef.scopeId, entityRef.ttl, strings);
        return this;
    }

    @Override
    public SessionUser removeKey(ImmutableSet<String> key) {
        final var entityRef = this.entity;
        this.checkSession(entityRef);
        tokenDAO.removeKeyAsync(entityRef.uid, entity.scopeId, key);
        return this;
    }

    private static final class InlineEntity {
        private final long uid;
        private final long scopeId;
        private final Map<String, String> attach;
        private final Map<String, Object> cached = Maps.newHashMap();
        private Duration ttl = Duration.ofHours(1);

        InlineEntity(long uid, long scopeId, Map<String, String> attach) {
            this.uid = uid;
            this.scopeId = scopeId;
            this.attach = attach;
        }

        public InlineEntity setTtl(Duration ttl) {
            this.ttl = ttl;
            return this;
        }

    }

}
