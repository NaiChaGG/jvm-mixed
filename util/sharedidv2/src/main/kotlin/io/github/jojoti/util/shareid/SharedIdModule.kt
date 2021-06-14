package io.github.jojoti.util.shareid

import dagger.Binds
import dagger.Module
import javax.inject.Singleton

/**
 *
 * @rf https://github.com/Kotlin/kotlin-examples/blob/master/maven/dagger-maven-example/src/main/kotlin/Thermosiphon.kt
 * @author JoJo Wang
 * @link github.com/jojoti
 */
@Module
abstract class SharedIdModule {

    @Binds
    @Singleton
    abstract fun hashShareId(impl: HashShareIdImpl): HashShareId

}