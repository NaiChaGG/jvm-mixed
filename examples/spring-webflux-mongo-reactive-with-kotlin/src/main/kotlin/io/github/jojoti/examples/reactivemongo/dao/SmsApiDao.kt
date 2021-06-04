package io.github.jojoti.examples.reactivemongo.dao

import reactor.core.publisher.Mono

interface SmsApiDao {

    /**
     * 发送一条短信
     */
    fun send(telephone: String, content: String): Mono<Boolean>

}