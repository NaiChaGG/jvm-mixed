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
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * @author JoJo Wang
 * @link github.com/jojoti
 */
@Configuration(proxyBeanMethods = false)
// grpc server 正常启动
@ConditionalOnClass(StringRedisTemplate.class)
public class SessionRedisAutoConfiguration {

    @Bean
    public TokenDAO expireTokenAsync(StringRedisTemplate stringRedisTemplate) {
        return new TokenDAO(stringRedisTemplate);
    }

    @Bean
    public SessionRedis ramAccess(TokenDAO expireToken, ObjectMapper objectMapper) {
        return new SessionRedis(expireToken, objectMapper);
    }

    @Bean
    @ConditionalOnMissingBean(ObjectMapper.class)
    public static ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

}
