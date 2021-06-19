package io.github.jojoti.grpcstartersbramredis;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author JoJo Wang
 * @link github.com/jojoti
 */
@Component
public class ExpireTokenAsync {

    private final StringRedisTemplate stringRedisTemplate;

    public ExpireTokenAsync(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Async
    public void expireToken(String key) {
        // 异步延长 token 过期时间
//        CompletableFuture.runAsync(() -> {
        this.stringRedisTemplate.expire(key, 600, TimeUnit.SECONDS);
//        });
    }

}
