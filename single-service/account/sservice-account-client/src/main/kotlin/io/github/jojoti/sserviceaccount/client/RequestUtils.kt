package io.github.jojoti.boundary.util

import io.github.jojoti.commelina.utils.core.kt.KVArgsSign

object RequestUtils {

    fun createGetUri(keySecret: String, kv: MutableMap<String, Any>): String {
        val time = System.currentTimeMillis()
        kv["_time"] = time
        val sign = KVArgsSign.signSha256(keySecret, kv)

        val str = StringBuilder("_time=$time&sign=$sign")
        kv.forEach { t, u -> str.append("&$t=$u") }
        return str.toString()
    }

}