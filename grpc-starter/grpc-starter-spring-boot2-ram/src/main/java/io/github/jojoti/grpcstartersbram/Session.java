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

import com.google.common.base.Preconditions;
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

    static String checkAttachKey(String key) {
        Preconditions.checkArgument(!key.startsWith("_"), "Key is not allow prefix _");
        return key;
    }

    static ImmutableList<String> checkAttachKey(ImmutableList<String> attachInline) {
        if (attachInline.size() > 0) {
            for (String s : attachInline) {
                checkAttachKey(attachInline);
            }
        }
        return attachInline;
    }

    /**
     * 验证用户会话 直接返回登录 uid >  0 表示已经登录
     *
     * @param tokenVal
     * @return
     */
    SessionUser verify(ParseToken tokenVal, ImmutableList<String> attachInline);

    default SessionUser verify(ParseToken tokenVal) {
        return verify(tokenVal, ImmutableList.of());
    }

    SessionUser verify(long uid, int scopeId, ImmutableList<String> attachInline);

    default SessionUser verify(long uid, int scopeId) {
        return verify(uid, scopeId, ImmutableList.of());
    }

    /**
     * 根据传入的参数退出
     *
     * @param uid
     * @param scopeId
     */
    void logout(long uid, int scopeId);

    final class ParseToken {
        private final HashIdToken.DecodeToken decodeToken;

        public ParseToken(HashIdToken.DecodeToken decodeToken) {
            this.decodeToken = decodeToken;
        }

        public HashIdToken.DecodeToken getDecodeToken() {
            return decodeToken;
        }

    }

}
