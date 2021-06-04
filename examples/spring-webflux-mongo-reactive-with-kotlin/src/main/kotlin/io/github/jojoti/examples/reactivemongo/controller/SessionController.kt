package io.github.jojoti.examples.reactivemongo.controller

import io.github.jojoti.examples.reactivemongo.*
import io.github.jojoti.examples.reactivemongo.biz.MemberBiz
import io.github.jojoti.examples.reactivemongo.dto.MemberDto
import io.github.jojoti.examples.reactivemongo.dvo.LoginSessionEntity
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.WebSession
import reactor.core.publisher.Mono

@RestController
class SessionController {

    /**
     * 参考这种写法，变量 final 更严谨
     * @link https://docs.spring.io/spring-boot/docs/2.0.0.M7/reference/htmlsingle/#boot-features-mongo-template
     */
    @Autowired
    private lateinit var memberBiz: MemberBiz

    /**
     * 根据账号密码进行登录, 登录操作不幂等，发起新的操作，旧 token 失效
     */
    @PostMapping(API_SESSION_LOGIN_WITH_EMAIL)
    fun loginWithEmail(
        @RequestParam account: String,
        @RequestParam password: String,
        session: WebSession
    ): Mono<DomainEntityVO<LoginSessionEntity>> {
        return this.doAddSession(memberBiz.signInByEmailAccount(account, password), session)
    }

    private fun doAddSession(member: Mono<MemberDto>, session: WebSession): Mono<DomainEntityVO<LoginSessionEntity>> {
        return member.map {
            if (it.isSuccess) {
                session.attributes["sessionUserId"] = it.uid
                DomainCreator.newVO(LoginSessionEntity(session.id))
            } else {
                DomainCreator.newVO<LoginSessionEntity>(it.errorCode)
            }
        }
    }

}