/*
 * Copyright 2021 JoJo Wang , homepage: https://github.com/jojoti/jvm-mixed.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.jojoti.examples.dagger.service;

import io.github.jojoti.examples.dagger.dao.UserDao1;
import io.github.jojoti.examples.dagger.dao.UserDao2;

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