package io.github.jojoti.grpcstartersbram;

import java.lang.annotation.*;

/**
 * @author Wang Yue
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SessionAttach {

    /**
     * 附件 级联 keys
     * 配置到此处才会 去 redis 里面在当前请求里读取 redis hash 里面保存的用户上下文的缓存
     */
    String[] value() default {};

}
