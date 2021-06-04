package io.github.jojoti.examples.dagger.dao;

import com.github.dagger.di.AppScope;
import dagger.Binds;
import dagger.Module;

/**
 * @author JoJo Wang
 * @link github.com/jojoti
 * @date 2020/5/19
 */
@Module
public abstract class DaoModule {

    @AppScope
    @Binds
    abstract UserDao1 userDao1(UserDao1Impl userDao1);

    @AppScope
    @Binds
    abstract UserDao2 userDao2(UserDao2Impl userDao1);

}
