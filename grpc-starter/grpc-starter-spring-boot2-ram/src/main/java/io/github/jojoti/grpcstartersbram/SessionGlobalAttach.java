package io.github.jojoti.grpcstartersbram;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * @author JoJo Wang
 * @link github.com/jojoti
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface SessionGlobalAttach {

    /**
     * 全局 增加 附近 数据的 key ram 使用 session attach 作为权限校验的存储
     * <p>
     * 附件 级联 keys
     * 配置到此处才会 去 redis 里面在当前请求里读取 redis hash 里面保存的用户上下文的缓存
     * <p>
     * key 不能 以 下划线 _ 开头
     */
    String[] value() default {};

}
