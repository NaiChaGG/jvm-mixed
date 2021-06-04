package io.github.jojoti.examples.dagger.service;

import com.github.dagger.dao.UserDao1;
import com.github.dagger.dao.UserDao2;

import javax.inject.Inject;

/**
 * @author JoJo Wang
 * @link github.com/jojoti
 * @date 2020/5/19
 */
public class UserServiceImpl implements UserService {

    @Inject
    UserDao1 userDao1;

    @Inject
    UserDao2 userDao2;

    @Inject
    public UserServiceImpl() {
    }

    @Override
    public String getUser(long uid) {
        return uid + ":getUser,hashCode:" + this.hashCode() + ",userDao1:" + userDao1.getName();
    }

}