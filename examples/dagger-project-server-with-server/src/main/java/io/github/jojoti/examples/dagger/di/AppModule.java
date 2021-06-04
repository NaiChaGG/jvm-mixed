package io.github.jojoti.examples.dagger.di;

import com.github.dagger.ScopeClassTest;
import com.github.dagger.UserEventContext;
import com.github.dagger.UserEventContextImpl;
import dagger.Binds;
import dagger.Module;
import dagger.Provides;

/**
 * @author JoJo Wang
 * @link github.com/jojoti
 * @date 2020/5/18
 */
@Module(subcomponents = {EventComponent.class})
public abstract class AppModule {

    @AppScope
    @Provides
    static ScopeClassTest scopeClassTest() {
        return new ScopeClassTest();
    }

//    @AppScope
//    @Binds
//    abstract ScopeClassTest scopeClassTest(ScopeClassTest scopeClassTest);

    @AppScope
    static EventComponent eventComponent(EventComponent.Builder builder) {
        return builder.build();
    }

    @AppScope
    @Binds
    abstract UserEventContext userEventContext(UserEventContextImpl userEventContext);

}