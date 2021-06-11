package io.github.jojoti.examples.dagger.event;

import io.github.jojoti.examples.dagger.service.UserService;

import javax.inject.Inject;

/**
 * @author JoJo Wang
 * @link github.com/jojoti
 * @date 2020/5/19
 */
public class UserEventHandlerImpl implements UserEventHandler {

    private final long userId;

    @Inject
    UserService userService;

    public UserEventHandlerImpl(long userId) {
        this.userId = userId;
    }

    @Override
    public String getUser() {
        return userService.getUser(this.userId);
    }


}
