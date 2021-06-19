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

import com.google.common.collect.ImmutableList;

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
     *
     * @param tokenVal
     * @return
     */
    SessionUser verify(String tokenVal, ImmutableList<String> attachInline);

    SessionUser verify(long uid, int scopeId, ImmutableList<String> attachInline);

//    /**
//     * 根据 token 删除 会话
//     * @param token
//     */
//    default void logout(String token) {
//        var parseToken = TokenUtils._parseToken(token);
//        final var uid = parseToken.ids[0];
//        final var sid = parseToken.ids[1];
//        this.logout(uid, (int) sid);
//    }

    /**
     * 根据传入的参数退出
     *
     * @param uid
     * @param scopeId
     */
    void logout(long uid, int scopeId);

}
