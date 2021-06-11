package io.github.jojoti.examples.dagger.di;

import dagger.Binds;
import dagger.Module;
import io.github.jojoti.examples.dagger.UserEventContext;
import io.github.jojoti.examples.dagger.UserEventContextImpl;

/**
 * @author JoJo Wang
 * @link github.com/jojoti
 * @date 2020/5/18
 */
@Module(subcomponents = {EventComponent.class})
public abstract class AppModule {

    @AppScope
    static EventComponent eventComponent(EventComponent.Builder builder) {
        return builder.build();
    }

    @AppScope
    @Binds
    abstract UserEventContext userEventContext(UserEventContextImpl userEventContext);

}