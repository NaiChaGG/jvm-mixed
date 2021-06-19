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

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import io.github.jojoti.grpcstartersbram.SessionCreator;
import io.github.jojoti.grpcstartersbram.SessionUser;
import io.github.jojoti.utilhashidtoken.HashIdToken;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.Map;

/**
 * @author JoJo Wang
 * @link github.com/jojoti
 */
class SessionRedis implements SessionCreator {

    private static final String ATTACH_SLAT_KEY = "__slat";
    private final StringRedisTemplate stringRedisTemplate;
    private final ExpireTokenAsync expireToken;

    SessionRedis(StringRedisTemplate stringRedisTemplate, ExpireTokenAsync expireToken) {
        this.stringRedisTemplate = stringRedisTemplate;
        this.expireToken = expireToken;
    }

    @Override
    public SessionUser verify(String tokenVal, ImmutableList<String> attachInline) {
        return null;
    }

    @Override
    public SessionUser verify(long uid, int scopeId, ImmutableList<String> attachInline) {
        return null;
    }

    @Override
    public void logout(long uid, int scopeId) {

    }

    private String makeKey(long uid, int sid) {
        return "dt" + uid + ":" + sid;
    }

    @Override
    public SessionUser valid(String token, ImmutableList<String> attachInline) {
        if (Strings.isNullOrEmpty(token)) {
            return null;
        }

        final var tokenParse = HashIdToken.parseToken(token);

        final var uid = tokenParse.getUid();
        final var scopeId = tokenParse.getScopeId();

        final var cacheKey = this.makeKey(uid, scopeId);

        final var hashKeys = ImmutableList.<String>builder().add(ATTACH_SLAT_KEY).addAll(attachInline).build();

        // 只获取这次需要一次查询的，否则使用延迟查询
        final var hashValues = this.stringRedisTemplate.<String, String>opsForHash().multiGet(cacheKey, hashKeys);

        // 至少要存在 slat
        if (hashValues.size() < 1) {
            return null;
        }

        var iterator = hashValues.iterator();
        var keyVal = iterator.next();

        if (Strings.isNullOrEmpty(keyVal) || !keyVal.equals(tokenParse.salt)) {
            return null;
        }

        iterator.remove();

        // 异步延长 token
        this.expireToken.expireToken(cacheKey);

        Map<String, Object> attach;

        if (hashValues.size() > 0) {
            attach = Maps.newHashMap();
            for (int i = 0; i < hashValues.size(); i++) {
                attach.put(hashKeys.get(i + 1), hashValues.get(i));
            }
        } else {
            attach = Map.of();
        }

        return new SessionUser() {
            @Override
            public int getScopeId() {
                return scopeId;
            }

            @Override
            public long getUid() {
                return uid;
            }

            @Override
            public boolean isAnonymous() {
                return uid <= 0;
            }

            @Override
            public void logout() {

            }

            @Override
            public String getCurrentToken() {
                return token;
            }

            @Override
            public String refreshToken() {
                return null;
            }

            @Override
            public String login(long uid) {
                return null;
            }

            @Override
            public String getAttach(String key) {
                return null;
            }

            @Override
            public <T> T getAttachJson(String key, Class<T> t) {
                return null;
            }

            @Override
            public Object getAttachObject(String key) {
                return null;
            }

            @Override
            public Map<String, Object> getAllAttach() {
                return null;
            }

            @Override
            public SessionUser setAttachString(ImmutableMap<String, String> jsonValues) {
                return null;
            }

            @Override
            public <T> SessionUser setAttachJson(ImmutableMap<String, T> jsonValues) {
                return null;
            }
        };
    }

}