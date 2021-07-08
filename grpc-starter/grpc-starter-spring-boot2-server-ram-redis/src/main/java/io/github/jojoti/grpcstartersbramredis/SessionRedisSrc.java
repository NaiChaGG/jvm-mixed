package io.github.jojoti.grpcstartersbramredis;

import java.lang.annotation.*;

/**
 * rf: org.springframework.context.annotation.Primary
 *
 * @author JoJo Wang
 * @link github.com/jojoti
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SessionRedisSrc {
}
