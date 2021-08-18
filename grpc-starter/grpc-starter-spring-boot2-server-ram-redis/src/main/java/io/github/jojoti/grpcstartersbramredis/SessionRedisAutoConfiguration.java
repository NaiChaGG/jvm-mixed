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

package io.github.jojoti.grpcstartersbramredis;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * fixme session 后续可能会支持独立数据源 待定
 *
 * @author JoJo Wang
 * @link github.com/jojoti
 */
@Configuration(proxyBeanMethods = false)
// grpc server 正常启动
@ConditionalOnClass(StringRedisTemplate.class)
public class SessionRedisAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(ObjectMapper.class)
    public static ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    // 不存在 TokenDAO & 存在 SessionRedisSrc 触发这个 支持 自定义 redis 源
    @Bean
    @ConditionalOnMissingBean(value = TokenDAO.class)
    @ConditionalOnBean(annotation = SessionRedisSrc.class)
    public TokenDAO tokenDao(@Qualifier("sessionRedis") StringRedisTemplate stringRedisTemplate) {
        return new TokenDAO(stringRedisTemplate);
    }

    // 不存在 SessionRedisSrc & TokenDAO 使用这个
    @Bean
    @ConditionalOnMissingBean(value = TokenDAO.class, annotation = SessionRedisSrc.class)
    public TokenDAO tokenDaoPrimary(StringRedisTemplate stringRedisTemplate) {
        return new TokenDAO(stringRedisTemplate);
    }

    @Bean
    public SessionRedis sessionRedis(TokenDAO expireToken, ObjectMapper objectMapper) {
        return new SessionRedis(expireToken, objectMapper);
    }

}
