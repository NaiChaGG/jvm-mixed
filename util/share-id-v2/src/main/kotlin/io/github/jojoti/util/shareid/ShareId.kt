package io.github.jojoti.util.shareid

/**
 * @author JoJo Wang
 * @link github.com/jojoti
 */
interface ShareId {

    fun encode(id: Long): String

    fun encode(id: Int): String {
        return encode(id.toLong());
    }

    fun encode(id: Short): String {
        return encode(id.toLong());
    }

    fun encode(id: Byte): String {
        return encode(id.toLong());
    }

    fun encode(id: Char): String {
        return encode(id.toLong());
    }

    fun decodeLong(id: String): Long

}