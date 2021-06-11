package io.github.jojoti.examples.dagger.di;

import dagger.Component;
import io.github.jojoti.examples.dagger.dao.DaoModule;
import io.github.jojoti.examples.dagger.router.RouterApi;
import io.github.jojoti.examples.dagger.service.ServiceModule;

/**
 * @author JoJo Wang
 * @link github.com/jojoti
 * @date 2020/5/18
 */
@Component(modules = {AppModule.class, ServiceModule.class, DaoModule.class})
@AppScope
public interface AppComponent {

    void injectApiHandler(RouterApi routerApi);

}