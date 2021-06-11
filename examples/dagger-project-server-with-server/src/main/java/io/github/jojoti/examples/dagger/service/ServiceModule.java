package io.github.jojoti.examples.dagger.service;

import dagger.Binds;
import dagger.Module;
import io.github.jojoti.examples.dagger.di.AppScope;

/**
 * @author JoJo Wang
 * @link github.com/jojoti
 * @date 2020/5/19
 */
@Module
public abstract class ServiceModule {

    @AppScope
    @Binds
    abstract UserService userService(UserServiceImpl userService);

}