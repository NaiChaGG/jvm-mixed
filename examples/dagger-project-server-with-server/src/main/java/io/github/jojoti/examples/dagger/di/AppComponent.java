package io.github.jojoti.examples.dagger.di;

import com.github.dagger.dao.DaoModule;
import com.github.dagger.router.RouterApi;
import com.github.dagger.service.ServiceModule;
import dagger.Component;

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