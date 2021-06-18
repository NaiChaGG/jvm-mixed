package io.github.trapspring.datajdbcpostgres;

import java.lang.annotation.*;

/**
 * 标记了这个注解会自动使用 json converter
 *
 * @author JoJo Wang
 * @link github.com/jojoti
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface JsonColumn {
}
