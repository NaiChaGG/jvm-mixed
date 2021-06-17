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
import io.github.jojoti.grpcstartersbram.SessionInterceptor;
import io.github.jojoti.grpcstartersbram.SessionUser;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * 拦截器需要 使用者 自行添加到 grpc server 里面去，多server无法知道用户注入哪里
 *
 * @author JoJo Wang
 * @link github.com/jojoti
 */
public class RedisSessionInterceptor extends SessionInterceptor {

    private final StringRedisTemplate stringRedisTemplate;

    public RedisSessionInterceptor(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
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

    @Override
    public SessionUser valid(String token) {
        return null;
    }

}
