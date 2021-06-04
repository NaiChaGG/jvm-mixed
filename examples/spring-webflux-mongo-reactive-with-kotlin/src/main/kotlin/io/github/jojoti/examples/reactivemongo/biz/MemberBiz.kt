package io.github.jojoti.examples.reactivemongo.biz

import io.github.jojoti.examples.reactivemongo.dto.MemberDto
import io.github.jojoti.examples.reactivemongo.dto.MemberOrNewDto

import reactor.core.publisher.Mono

interface MemberBiz {

    /**
     * 根据自定义账号获取登录用户
     */
    fun signInByMixedAccount(mixedAccount: String, pwd: String): Mono<MemberDto>

    /**
     * 根据邮箱密码登录账号
     */
    fun signInByEmailAccount(email: String, pwd: String): Mono<MemberDto>

    /**
     * 根据电话号码和密码登录
     */
    fun signInByTelephoneAccount(telephone: String, pwd: String): Mono<MemberDto>

    /**
     * 手机号免密登录
     */
    fun signInByTelephoneAccountNoPass(telephone: String, smsCode: String): Mono<MemberOrNewDto>

}