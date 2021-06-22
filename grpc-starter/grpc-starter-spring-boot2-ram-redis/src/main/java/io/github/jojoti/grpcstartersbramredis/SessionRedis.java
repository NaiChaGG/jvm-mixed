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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import io.github.jojoti.grpcstartersbram.Session;
import io.github.jojoti.grpcstartersbram.SessionUser;

/**
 * @author JoJo Wang
 * @link github.com/jojoti
 */
class SessionRedis implements Session {

    private final TokenDAO tokenDAO;
    private final ObjectMapper objectMapper;

    SessionRedis(TokenDAO tokenDAO, ObjectMapper objectMapper) {
        this.tokenDAO = tokenDAO;
        this.objectMapper = objectMapper;
    }

    @Override
    public SessionUser verify(String tokenVal, ImmutableList<String> attachInline) {
        return new AbstractSessionUser(tokenDAO, objectMapper, tokenVal, attachInline);
    }

    @Override
    public SessionUser verify(long uid, int scopeId, ImmutableList<String> attachInline) {
        return new AbstractSessionUser(tokenDAO, objectMapper, uid, scopeId, attachInline);
    }

    @Override
    public void logout(long uid, int scopeId) {
        this.tokenDAO.logoutSync(uid, scopeId);
    }

}