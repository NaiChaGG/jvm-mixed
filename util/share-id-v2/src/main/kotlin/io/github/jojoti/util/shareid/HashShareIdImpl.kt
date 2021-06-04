package io.github.jojoti.util.shareid

import javax.inject.Inject

/**
 * @author JoJo Wang
 * @link github.com/jojoti
 */
open class HashShareIdImpl @Inject constructor() : HashShareId {

    override fun encode(id: Long): String {
        return "";
    }

    override fun decodeLong(id: String): Long {
        return 1L;
    }

}