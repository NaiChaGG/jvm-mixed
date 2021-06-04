package io.github.jojoti.examples.reactivemongo.controller

import io.github.jojoti.examples.reactivemongo.API_ACCOUNT_CREATE_EMAIL
import io.github.jojoti.examples.reactivemongo.biz.AccountBiz
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

//@RestController
//class AccountController {
//
//    @Autowired
//    private lateinit var accountBiz: AccountBiz
//
//    /**
//     * 根据email和密码创建一个新的用户
//     */
//    @PostMapping(API_ACCOUNT_CREATE_EMAIL)
//    fun createNewEmailAccount(@RequestParam email: String, @RequestParam password: String): Mono<DomainEntityEmptyVO> {
//        return accountBiz.createNewEmailAccount(email, password).map {
//            DomainCreator.VO_SUCCESS
//        }.switchIfEmpty(Mono.just(DomainCreator.VO_SUCCESS))
//    }
//
//}