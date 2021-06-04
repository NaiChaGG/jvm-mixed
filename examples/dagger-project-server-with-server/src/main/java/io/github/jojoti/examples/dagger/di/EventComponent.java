package io.github.jojoti.examples.dagger.di;

import com.github.dagger.event.UserEventHandlerImpl;
import dagger.Subcomponent;

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