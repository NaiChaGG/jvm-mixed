/*
 * Copyright 2021 JoJo Wang , homepage: https://github.com/jojoti/experiment-jvm.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.jojoti.grpcstartersbramredis;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author JoJo Wang
 * @link github.com/jojoti
 */
class TokenDAO {

    private static final Logger log = LoggerFactory.getLogger(TokenDAO.class);

    private final StringRedisTemplate stringRedisTemplate;

    public TokenDAO(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    private static String makeKey(long uid, long sid) {
        return "dt:" + uid + ":" + sid;
    }

    @Async
    public void expireTokenAsync(long uid, long scopeId, Duration ttl) {
        // 异步延长 token 过期时间
        this.stringRedisTemplate.expire(TokenDAO.makeKey(uid, scopeId), ttl.getSeconds(), TimeUnit.SECONDS);
    }

    public Map<String, String> getSession(long uid, long scopeId, ImmutableList<String> hashKeys) {
        final var makeKey = TokenDAO.makeKey(uid, scopeId);
        final var hashValues = this.stringRedisTemplate.<String, String>opsForHash().multiGet(makeKey, hashKeys);

        if (hashValues.size() <= 0) {
            return Map.of();
        }

        final Map<String, String> attach = Maps.newHashMap();
        for (int i = 0; i < hashValues.size(); i++) {
            attach.put(hashKeys.get(i), hashValues.get(i));
        }

        return attach;
    }

    public void logoutSync(long uid, long scopeId) {
        final var deleteKey = TokenDAO.makeKey(uid, scopeId);
        final var rs = this.stringRedisTemplate.delete(deleteKey);
        if (rs != null && rs && log.isInfoEnabled()) {
            log.info("Logout uid {}, sid {}", uid, scopeId);
        }
    }

    @Async
    public void logoutAsync(long uid, long scopeId) {
        this.logoutSync(uid, scopeId);
    }

    @Async
    public void addAttachAsync(long uid, long scopeId, Duration ttl, Map<String, String> attach) {
        this.addAttachSync(uid, scopeId, ttl, attach);
    }

    @Async
    public void removeKeyAsync(long uid, long scopeId, ImmutableSet<String> keys) {
        final var key = makeKey(uid, scopeId);
        stringRedisTemplate.opsForHash().delete(key, keys.toArray());
    }

    public void addAttachSync(long uid, long scopeId, Duration ttl, Map<String, String> attach) {
        final var key = makeKey(uid, scopeId);
        stringRedisTemplate.execute(new SessionCallback<List<Object>>() {
            // https://docs.spring.io/spring-data/data-redis/docs/current/reference/html/#tx
            public List<Object> execute(RedisOperations operations) throws DataAccessException {
                operations.multi();
                operations.opsForHash().putAll(key, attach);
                operations.expire(key, ttl.toMillis(), TimeUnit.MILLISECONDS);
                return operations.exec();
            }
        });
    }

}
