package io.github.jojoti.sserviceaccount.controller.api

import io.github.jojoti.commelina.webmvc.session.AllowAnonymousAccess
import io.github.jojoti.commelina.webmvc.session.AuthSession
import io.github.jojoti.commelina.webmvc.ResponseMessage
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping("/api/connect")
class SessionController {

    @Autowired
    private lateinit var authSession: AuthSession

    @PostMapping("/out")
    @AllowAnonymousAccess
    fun signOut(request: HttpServletRequest): ResponseMessage<Any?> {
        authSession.out(request)
        return ResponseMessage.ANY
    }

}