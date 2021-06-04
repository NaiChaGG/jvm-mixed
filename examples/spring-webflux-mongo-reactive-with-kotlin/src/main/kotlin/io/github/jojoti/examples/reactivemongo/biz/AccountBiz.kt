package io.github.jojoti.examples.reactivemongo.biz

import io.github.jojoti.examples.reactivemongo.dto.MemberDto
import reactor.core.publisher.Mono

interface AccountBiz {

    /**
     * 根据自定义账号创建帐户
     */
    fun createNewMixedAccount(mixed: String, password: String): Mono<MemberDto>

    /**
     * 根据 email 创建帐户
     */
    fun createNewEmailAccount(email: String, password: String): Mono<MemberDto>

    /**
     * 根据手机号密码创建帐户
     */
    fun createNewTelephoneAccount(telephone: String, password: String): Mono<MemberDto>

    /**
     * 免密账号创建
     */
    fun createNewTelephoneNoPassAccount(telephone: String): Mono<MemberDto>

}