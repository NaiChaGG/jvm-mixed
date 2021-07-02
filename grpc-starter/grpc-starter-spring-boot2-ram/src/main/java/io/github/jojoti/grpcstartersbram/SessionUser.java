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

import com.google.common.collect.ImmutableMap;

/**
 * @author JoJo Wang
 * @link github.com/jojoti
 */
public interface SessionUser {

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
    String getAttach(String key);

    /**
     * @param key
     * @return
     * @throws SessionNotCreatedException
     */
    default int getAttachAsInt(String key) {
        return Integer.parseInt(getAttach(key));
    }

    /**
     * @param key
     * @return
     * @throws SessionNotCreatedException
     */
    default long getAttachAsLong(String key) {
        return Long.parseLong(getAttach(key));
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

    /**
     * 会写库 谨慎操作
     *
     * @param key
     * @param val
     * @return
     * @throws SessionNotCreatedException
     */
    default SessionUser setAttachString(String key, String val) {
        return setAttachString(ImmutableMap.of(key, val));
    }

    /**
     * 会写库 谨慎操作
     *
     * @param stringValues
     * @return
     * @throws SessionNotCreatedException
     */
    SessionUser setAttachString(ImmutableMap<String, String> stringValues);

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

    interface NewTokenBuilder {

        NewTokenBuilder setAttachString(String key, String val);

        <T> NewTokenBuilder setAttachJson(String key, T t);

        String build();

    }

}