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

package io.github.jojoti.grpcstartersbram;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

import java.time.Duration;
import java.util.Arrays;

/**
 * @author JoJo Wang
 * @link github.com/jojoti
 */
public interface SessionUser {

    String ATTACH_SLAT_KEY = "_slat";
    String ATTACH_TTL_KEY = "_ttl";

    static String checkSetAttachKey(String key) {
        if (ATTACH_SLAT_KEY.equals(key)) {
            throw new IllegalArgumentException("Custom attach key not allow " + ATTACH_SLAT_KEY);
        }
        if (ATTACH_TTL_KEY.equals(key)) {
            throw new IllegalArgumentException("Custom attach key not allow " + ATTACH_TTL_KEY);
        }
        return key;
    }

    /**
     * 获取当前会话的 scopeId
     *
     * @return
     * @throws SessionNotCreatedException
     */
    long getScopeId();

    /**
     * 获取用户的 uid
     *
     * @return
     * @throws SessionNotCreatedException
     */
    long getUid();

    /**
     * 获取用户 id 未登陆会返回0
     *
     * @return
     */
    long getUidAsDefault();

    /**
     * 判断匿名会话
     *
     * @return
     */
    boolean isAnonymous();

    default boolean isLogin() {
        return !isAnonymous();
    }

    /**
     * 用户登出
     *
     * @throws SessionNotCreatedException
     */
    void logout();

    /**
     * 登录
     *
     * @param uid
     * @return 返回 token
     */
    NewTokenBuilder newToken(long uid, long scopeId);

    default NewTokenBuilder newToken(long uid) {
        return newToken(uid, 0L);
    }

    /**
     * 不会读库
     *
     * @param key
     * @return
     * @throws SessionNotCreatedException
     */
    String getAttachString(String key);

    /**
     * @param key
     * @return
     * @throws SessionNotCreatedException
     */
    default int getAttachAsInt(String key) {
        final var val = getAttachString(key);
        if (Strings.isNullOrEmpty(val)) {
            return 0;
        }
        return Integer.parseInt(val);
    }

    /**
     * @param key
     * @return
     * @throws SessionNotCreatedException
     */
    default long getAttachAsLong(String key) {
        final var val = getAttachString(key);
        if (Strings.isNullOrEmpty(val)) {
            return 0L;
        }
        return Long.parseLong(val);
    }

    default boolean getAttachAsBoolean(String key) {
        final var val = getAttachString(key);
        if (Strings.isNullOrEmpty(val)) {
            return false;
        }
        return Boolean.parseBoolean(val);
    }

    /**
     * 不会读库
     *
     * @param key
     * @param t
     * @param <T>
     * @return
     * @throws SessionNotCreatedException
     */
    <T> T getAttachJson(String key, Class<T> t);

    default SessionUser setAttach(String key, long val) {
        if (val == 0L) {
            return this;
        }
        return setAttach(ImmutableMap.of(key, String.valueOf(val)));
    }

    default SessionUser setAttach(String key, int val) {
        if (val == 0) {
            return this;
        }
        return setAttach(ImmutableMap.of(key, String.valueOf(val)));
    }

    /**
     * 附件里面写 bool 值 如果值 为 false 会被忽略
     *
     * @param key
     * @param val
     * @return
     */
    default SessionUser setAttach(String key, boolean val) {
        if (!val) {
            return this;
        }
        return setAttach(ImmutableMap.of(key, String.valueOf(true)));
    }

    /**
     * 会写库 谨慎操作
     *
     * @param key
     * @param val
     * @return
     * @throws SessionNotCreatedException
     */
    default SessionUser setAttach(String key, String val) {
        return setAttach(ImmutableMap.of(key, val));
    }

    /**
     * 会写库 谨慎操作
     *
     * @param stringValues
     * @return
     * @throws SessionNotCreatedException
     */
    SessionUser setAttach(ImmutableMap<String, String> stringValues);

    /**
     * 会写库 谨慎操作
     *
     * @param key
     * @param t
     * @param <T>
     * @return
     * @throws SessionNotCreatedException
     */
    default <T> SessionUser setAttachJson(String key, T t) {
        return setAttachJson(ImmutableMap.of(key, t));
    }

    /**
     * 会写库 谨慎操作
     *
     * @param jsonValues
     * @param <T>
     * @return
     * @throws SessionNotCreatedException
     */
    <T> SessionUser setAttachJson(ImmutableMap<String, T> jsonValues);

    default SessionUser removeKey(String key) {
        return removeKey(ImmutableSet.of(key));
    }

    default SessionUser removeKey(String key, String... keys) {
        final var newKeys = ImmutableSet.<String>builder().add(key).addAll(Arrays.asList(keys)).build();
        return removeKey(newKeys);
    }

    SessionUser removeKey(ImmutableSet<String> key);

    interface NewTokenBuilder {

        NewTokenBuilder setAttachString(String key, String val);

        NewTokenBuilder setTtl(Duration ttl);

        <T> NewTokenBuilder setAttachJson(String key, T t);

        String build();

    }

}