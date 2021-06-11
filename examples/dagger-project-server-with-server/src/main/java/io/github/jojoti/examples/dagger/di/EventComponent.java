package io.github.jojoti.examples.dagger.di;

import dagger.Subcomponent;
import io.github.jojoti.examples.dagger.event.UserEventHandlerImpl;

/**
 * @author JoJo Wang
 * @link github.com/jojoti
 * @date 2020/5/18
 */
@Subcomponent(modules = {EventModule.class})
@EventScope
public interface EventComponent {

    void inject(UserEventHandlerImpl userEventHandler);

    @Subcomponent.Builder
    interface Builder {
        EventComponent build();
    }

}