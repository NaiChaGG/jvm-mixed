package io.github.jojoti.examples.dagger.service;

import com.github.dagger.di.AppScope;
import dagger.Binds;
import dagger.Module;

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