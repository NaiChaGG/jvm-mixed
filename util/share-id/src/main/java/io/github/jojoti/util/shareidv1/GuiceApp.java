package io.github.jojoti.util.shareidv1;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Stage;

/**
 * @author JoJo Wang
 */
public class GuiceApp {

    private static Injector app() {
        return SharedIdHolder.INJECTOR;
    }

    public static SharedIdSignHashFactory sharedIdSignHash() {
        return app().getInstance(SharedIdSignHashFactory.class);
    }

    public static SharedIdHashFacotry sharedIdHash() {
        return app().getInstance(SharedIdHashFacotry.class);
    }

    public static SharedIdExpireSignFactory sharedIdExpireHash() {
        return app().getInstance(SharedIdExpireSignFactory.class);
    }

    private interface SharedIdHolder {
        Injector INJECTOR = Guice.createInjector(Stage.PRODUCTION, new SharedIdModule());
    }

}
