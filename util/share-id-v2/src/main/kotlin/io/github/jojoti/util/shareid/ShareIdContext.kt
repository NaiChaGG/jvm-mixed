package io.github.jojoti.util.shareid

/**
 * @author JoJo Wang
 * @link github.com/jojoti
 */
interface ShareIdContext {

    fun provider(salt: String): ShareIdContextProvider

    interface ShareIdContextProvider {

    }

}