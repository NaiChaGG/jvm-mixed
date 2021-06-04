package io.github.jojoti.examples.dagger.router;

import com.github.dagger.UserEventContext;
import com.github.dagger.handler.HelloHandler;

import javax.inject.Inject;

/**
 * @author JoJo Wang
 * @link github.com/jojoti
 * @date 2020/5/19
 */
public class RouterApi {

    @Inject
    HelloHandler helloHandler;

}