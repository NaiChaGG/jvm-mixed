/*
 * Copyright 2021 JoJo Wang , homepage: https://github.com/jojoti/jvm-mixed.
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
import com.google.common.collect.ImmutableList;
import io.github.jojoti.utilhashidtoken.HashIdToken;

/**
 * 用户会话实现
 * <p>
 * hash key -> {"slat":"","appendKey":(1), "appendKeys":""}
 *
 * @author JoJo Wang
 * @link github.com/jojoti
 */
public interface Session {

    /**
     * 验证用户会话 直接返回登录 uid >  0 表示已经登录
     */
    SessionUser verify(ParseToken tokenVal, ImmutableList<String> attachInline);

    /**
     * 对于 运行时的 key 不会做校验
     */
    default SessionUser verify(String tokenVal, ImmutableList<String> attachInline) {
        return verify(Session.ParseToken.newParseToken(tokenVal), attachInline);
    }

    default SessionUser verify(String tokenVal) {
        return verify(Session.ParseToken.newParseToken(tokenVal), ImmutableList.of());
    }

    default SessionUser verify(ParseToken tokenVal) {
        return verify(tokenVal, ImmutableList.of());
    }

    /**
     * 全局 key 这里不支持 需要自行 设置 因为 动态 scope 无法知道
     */
    SessionUser verify(long uid, long scopeId, ImmutableList<String> attachInline);

    /**
     * 全局 key 这里不支持 需要自行 设置 因为 动态 scope 无法知道
     */
    default SessionUser verify(long uid, long scopeId) {
        return verify(uid, scopeId, ImmutableList.of());
    }

    /**
     * 全局 key 这里不支持 需要自行 设置 因为 动态 scope 无法知道
     */
    default SessionUser verify(long uid) {
        return verify(uid, 0L);
    }

    /**
     * 根据传入的参数退出
     */
    void logoutSync(long uid, long scopeId);

    default void logoutSync(long uid) {
        logoutSync(uid, 0L);
    }

    void logoutAsync(long uid, long scopeId);

    default void logoutAsync(long uid) {
        logoutAsync(uid, 0L);
    }

    final class ParseToken {

        private static final Session.ParseToken anonymous = new Session.ParseToken(null);

        private final HashIdToken.DecodeToken decodeToken;

        ParseToken(HashIdToken.DecodeToken decodeToken) {
            this.decodeToken = decodeToken;
        }

        public static ParseToken newParseToken(String token) {
            return Strings.isNullOrEmpty(token) ? anonymous : new Session.ParseToken(HashIdToken.parseToken(token));
        }

        public HashIdToken.DecodeToken getDecodeToken() {
            return decodeToken;
        }

    }

}
