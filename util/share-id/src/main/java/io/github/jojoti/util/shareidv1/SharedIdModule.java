package io.github.jojoti.util.shareidv1;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.google.inject.assistedinject.FactoryModuleBuilder;

/**
 * @author JoJo Wang
 */
public class SharedIdModule extends AbstractModule {

    @Override
    protected void configure() {

        bind(SignatureArg.class).toInstance(null);

        install(new FactoryModuleBuilder()
                .implement(new TypeLiteral<SharedId<Long, SharedIdSignDecodeValue>>() {
                }, SharedIdSignHash.class)
                .build(SharedIdSignHashFactory.class));

        install(new FactoryModuleBuilder()
                .implement(new TypeLiteral<SharedId<Long, Long>>() {
                }, SharedIdHash.class)
                .build(SharedIdHashFacotry.class));

        install(new FactoryModuleBuilder()
                .implement(new TypeLiteral<SharedId<SharedIdExpireEncodeValue, SharedIdExpireDecodeValue>>() {
                }, SharedIdExpireSignHash.class)
                .build(SharedIdExpireSignFactory.class));

    }

}
