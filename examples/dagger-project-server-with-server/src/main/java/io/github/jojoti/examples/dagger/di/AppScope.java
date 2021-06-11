package io.github.jojoti.examples.dagger.di;

import javax.inject.Scope;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author JoJo Wang
 * @link github.com/jojoti
 * @date 2020/5/18
 */
@Scope
@Documented
@Retention(RUNTIME)
public @interface AppScope {
}