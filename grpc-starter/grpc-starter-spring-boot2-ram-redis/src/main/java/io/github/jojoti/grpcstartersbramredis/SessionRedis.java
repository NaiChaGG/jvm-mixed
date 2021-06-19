package io.github.jojoti.grpcstartersbramredis;

import com.google.common.collect.ImmutableList;
import io.github.jojoti.grpcstartersbram.SessionCreator;
import io.github.jojoti.grpcstartersbram.SessionUser;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * @author JoJo Wang
 * @link github.com/jojoti
 */
class SessionRedis implements SessionCreator {

    private final StringRedisTemplate stringRedisTemplate;

    SessionRedis(StringRedisTemplate stringRedisTemplate) {
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