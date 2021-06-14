package io.github.jojoti.util.shareid

import dagger.Component
import javax.inject.Singleton

/**
 * @author JoJo Wang
 * @link github.com/jojoti
 */
@Singleton
@Component(modules = [SharedIdModule::class])
interface SharedIdComponent {

    fun sharedIdFactory(): SharedIdFactory

}