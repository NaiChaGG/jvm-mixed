package io.github.jojoti.examples.dagger.dao;

import javax.inject.Inject;

/**
 * @author JoJo Wang
 * @link github.com/jojoti
 * @date 2020/5/19
 */
public class UserDao1Impl implements UserDao1 {

    @Inject
    public UserDao1Impl() {

    }

    @Override
    public String getName() {
        return this.getClass().getSimpleName() + ",code:" + this.hashCode();
    }

}
