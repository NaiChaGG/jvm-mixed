package io.github.jojoti.sserviceaccount.controller.api

import io.github.jojoti.commelina.webmvc.session.AllowAnonymousAccess
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * fixme tbd
 */
@RestController
@RequestMapping("/api/account")
@Deprecated("tbd")
class AccountController {

    @PostMapping("/telephone")
    @AllowAnonymousAccess
    fun telephone() {

    }

    @PostMapping("/email")
    @AllowAnonymousAccess
    fun email() {

    }


    @PostMapping("/mixed")
    @AllowAnonymousAccess
    fun mixed() {

    }

}