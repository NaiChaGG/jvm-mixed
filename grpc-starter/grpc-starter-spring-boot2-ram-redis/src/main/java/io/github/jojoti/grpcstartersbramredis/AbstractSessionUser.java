package io.github.jojoti.grpcstartersbramredis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import io.github.jojoti.grpcstartersbram.Session;
import io.github.jojoti.grpcstartersbram.SessionNotCreatedException;
import io.github.jojoti.grpcstartersbram.SessionUser;
import io.github.jojoti.utilhashidtoken.HashIdToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * @author JoJo Wang
 * @link github.com/jojoti
 */
final class AbstractSessionUser implements SessionUser {

    private static final String ATTACH_SLAT_KEY = "slat";
    private static final Logger log = LoggerFactory.getLogger(AbstractSessionUser.class);

    private final TokenDAO tokenDAO;
    private final ObjectMapper objectMapper;
    // 更新时使用 影子实例来更新属性，避开多线程锁的问题
    private volatile InlineEntity entity;

    protected AbstractSessionUser(TokenDAO tokenDAO, ObjectMapper objectMapper, String token, ImmutableList<String> attachInline) {
        this.tokenDAO = tokenDAO;
        this.objectMapper = objectMapper;
        this.validToken(token, attachInline);
    }

    protected AbstractSessionUser(TokenDAO tokenDAO, ObjectMapper objectMapper, long uid, int scopeId, ImmutableList<String> attachInline) {
        this.tokenDAO = tokenDAO;
        this.objectMapper = objectMapper;
        final var session = tokenDAO.getSession(uid, scopeId, attachInline);
        if (session.size() == 0) {
            this.newAnonymous();
        } else {
            // 查询到了，表示会话存在
            this.entity = new InlineEntity(uid, scopeId, session, objectMapper);
        }
    }

    private void validToken(String token, ImmutableList<String> attachInline) {
        if (Strings.isNullOrEmpty(token)) {
            this.newAnonymous();
            return;
        }

        final var tokenParse = HashIdToken.parseToken(token);

        final var uid = tokenParse.getUid();
        final var scopeId = tokenParse.getScopeId();

        final var hashKeys = ImmutableList.<String>builder().add(AbstractSessionUser.ATTACH_SLAT_KEY).addAll(attachInline);

        // 只获取这次需要一次查询的，否则使用延迟查询
        final var hashValues = this.tokenDAO.getSession(uid, scopeId, hashKeys.build());

        // 至少要存在 slat
        if (hashValues.size() < 1) {
            this.newAnonymous();
            this.tokenDAO.logoutAsync(uid, scopeId);
            log.error("redis data error, check read & write");
            return;
        }

        final var foundSlat = hashValues.remove(AbstractSessionUser.ATTACH_SLAT_KEY);

        if (Strings.isNullOrEmpty(foundSlat) || !foundSlat.equals(tokenParse.salt)) {
            this.newAnonymous();
            this.tokenDAO.logoutAsync(uid, scopeId);
            log.error("redis data error, check read & write");
            return;
        }

        // 异步延长 token
        this.tokenDAO.expireTokenAsync(uid, scopeId);

        this.entity = new InlineEntity(uid, scopeId, hashValues, objectMapper);
    }

    private void newAnonymous() {
        this.entity = new InlineEntity(0, 0, Map.of(), objectMapper);
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
        final var newInline = new InlineEntity(uid, scopeId, hashValues, objectMapper);

        return new NewTokenBuilder() {
            @Override
            public NewTokenBuilder setAttachString(String key, String val) {
                newInline.attach.put("_" + key, val);
                return this;
            }

            @Override
            public <T> NewTokenBuilder setAttachJson(String key, T t) {
                try {
                    newInline.attach.put("_" + key, objectMapper.writeValueAsString(t));
                    // 这里 不做字符串缓存，因为写了还要读出来是低概率
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
                return this;
            }

            @Override
            public String build() {
                // 影子 实例更新
                AbstractSessionUser.this.entity = newInline;
                // 保存 session 到数据库 里
                tokenDAO.addAttachSync(uid, scopeId, newInline.attach);
                return newToken.getTokenBase64();
            }
        };
    }

    @Override
    public String getAttach(String key) {
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
    public SessionUser setAttachString(ImmutableMap<String, String> stringValues) {
        final var entityRef = this.entity;
        this.checkSession(entityRef);
        stringValues.forEach((K, V) -> {
            entityRef.attach.put(Session.checkAttachKey(K), V);
        });
        tokenDAO.addAttachAsync(entityRef.uid, entityRef.scopeId, stringValues);
        return this;
    }

    @Override
    public <T> SessionUser setAttachJson(ImmutableMap<String, T> jsonValues) {
        final var entityRef = this.entity;
        this.checkSession(entityRef);
        final var strings = Maps.<String, String>newHashMap();
        for (Map.Entry<String, T> stringTEntry : jsonValues.entrySet()) {
            try {
                entityRef.cached.put(Session.checkAttachKey(stringTEntry.getKey()), stringTEntry.getValue());
                strings.put(stringTEntry.getKey(), this.objectMapper.writeValueAsString(stringTEntry.getValue()));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
        tokenDAO.addAttachAsync(entityRef.uid, entityRef.scopeId, strings);
        return this;
    }

    private static final class InlineEntity {
        private final long uid;
        private final long scopeId;
        // 这里面存的都是
        private final Map<String, String> attach;
        private final Map<String, Object> cached = Maps.newHashMap();

        InlineEntity(long uid, long scopeId, Map<String, String> attach, ObjectMapper objectMapper) {
            this.uid = uid;
            this.scopeId = scopeId;
            this.attach = attach;
        }

    }

}
