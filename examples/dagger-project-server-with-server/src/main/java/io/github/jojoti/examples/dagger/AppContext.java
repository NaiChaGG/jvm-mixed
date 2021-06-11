package io.github.jojoti.examples.dagger;

import io.github.jojoti.examples.dagger.di.EventComponent;
import io.github.jojoti.examples.dagger.event.UserEventHandler;
import io.github.jojoti.examples.dagger.event.UserEventHandlerImpl;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

/**
 * @author JoJo Wang
 * @link github.com/jojoti
 * @date 2020/5/19
 */
public class AppContext {

    private final Map<Long, UserEventHandler> context = new HashMap<>();

    @Inject
    EventComponent eventComponent;

    @Inject
    public AppContext() {

    }

    public void add(long uid, UserEventHandler userEventHandler) {
        var user1 = new UserEventHandlerImpl(1);
        eventComponent.inject(user1);
    }

}