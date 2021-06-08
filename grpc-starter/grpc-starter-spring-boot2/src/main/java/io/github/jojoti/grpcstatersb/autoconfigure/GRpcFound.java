package io.github.jojoti.grpcstatersb.autoconfigure;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

import java.lang.annotation.*;

/**
 *
 * @author JoJo Wang
 * @link github.com/jojoti
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.METHOD })
@Documented
@ConditionalOnProperty(value = "grpcs.servers", havingValue = "", matchIfMissing = false)
public @interface GRpcFound {
}
