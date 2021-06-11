package io.github.jojoti.examples.dagger;

import io.github.jojoti.examples.dagger.di.DaggerAppComponent;
import io.github.jojoti.examples.dagger.router.RouterApi;

/**
 * @author JoJo Wang
 * @link github.com/jojoti
 * @date 2020/5/18
 */
public class AppMain {

    public static void main(String[] args) {

        var dagger = DaggerAppComponent.create();

        var router = new RouterApi();
        dagger.injectApiHandler(router);

//        var user1 = new UserEventHandlerImpl(1);
//
//        dagger.eventComponent().build().inject(user1);
//
//        var user2 = new UserEventHandlerImpl(2);
//        dagger.eventComponent().build().inject(user2);
//
//        System.out.println(user1.getUser());
//        System.out.println(user2.getUser());
    }

}
