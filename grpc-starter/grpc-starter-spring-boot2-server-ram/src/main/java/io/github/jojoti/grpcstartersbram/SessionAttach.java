package io.github.jojoti.grpcstartersbram;

import java.lang.annotation.*;

/**
 * @author JoJo Wang
 * @link github.com/jojoti
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SessionAttach {

    /**
     * 附件 级联 keys
     * 配置到此处才会 去 redis 里面在当前请求里读取 redis hash 里面保存的用户上下文的缓存
     * <p>
     * key 不能 以 下划线 _ 开头
     */
    String[] value() default {};

}
