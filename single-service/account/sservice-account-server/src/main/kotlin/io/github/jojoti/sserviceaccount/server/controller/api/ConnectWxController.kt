package io.github.jojoti.sserviceaccount.controller.api

import io.github.jojoti.boundary.opened.client.OpenedAppKeys
import io.github.jojoti.sserviceaccount.LoginFacade
import io.github.jojoti.sserviceaccount.proto.BELLIS_UC_API_ERRORS
import io.github.jojoti.boundary.utilswx.MPOAuth2Client
import io.github.jojoti.boundary.utilswx.MiniProgramAPI
import io.github.jojoti.commelina.webmvc.ResponseMessage
import io.github.jojoti.commelina.webmvc.error
import io.github.jojoti.commelina.webmvc.session.AllowAnonymousAccess
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * @doc url https://developers.weixin.qq.com/miniprogram/dev/api/api-api/wxRegisterOrCreate/code2Session.html
 */
@RestController
@RequestMapping("/api/connect/wx")
class ConnectWxController {

    @Autowired
    private lateinit var openedAppKeys: OpenedAppKeys

    @Autowired
    private lateinit var loginFacade: LoginFacade

    @Autowired
    private lateinit var miniProgramAPI: MiniProgramAPI

    @Autowired
    private lateinit var mpOAuth2Client: MPOAuth2Client

    /**
     * 微信小程序授权登录
     */
    @PostMapping("/mprogram/code2session")
    @AllowAnonymousAccess
    fun loginWithMiniProgram(
        @RequestParam wxMiniProgramAppId: String,
        @RequestParam code: String,
        request: HttpServletRequest,
        response: HttpServletResponse
    ): ResponseMessage<Any?> {

        val appSecret = openedAppKeys.wxMiniPrograms[wxMiniProgramAppId]
            ?: throw IllegalArgumentException("wxMiniProgramAppId not found.")

        val data = miniProgramAPI.code2Session(wxMiniProgramAppId, appSecret, code).data
            ?: return ResponseMessage.ANY.error { BELLIS_UC_API_ERRORS.GET_WX_MINI_PROGRAM_SERVER_ERROR }

        loginFacade.wxMiniProgram(request, wxMiniProgramAppId, data)

        return ResponseMessage.ANY
    }

    /**
     * @link https://open.weixin.qq.com/cgi-bin/showdocument?action=dir_list&t=resource/res_list&verify=1&id=open1419316505&token=74020794e3260bbbc9c7afc24966866cdd7dd65b&lang=zh_CN
     * @link https://open.weixin.qq.com/cgi-bin/showdocument?action=dir_list&t=resource/res_list&verify=1&id=open1419317851&token=74020794e3260bbbc9c7afc24966866cdd7dd65b&lang=zh_CN
     * fixme tbd
     * 移动终端
     */
    @PostMapping("/oauth2/mobile")
    @AllowAnonymousAccess
    fun loginWithMoblieOauth2Api(
        @RequestParam code: String
    ) {

    }

    /**
     * 公众号
     */
    @PostMapping("/oauth2/mp")
    @AllowAnonymousAccess
    fun loginWithMpOauth2Api(
        @RequestParam code: String
    ) {

    }

    /**
     * 网站
     */
    @PostMapping("/oauth2/site")
    @AllowAnonymousAccess
    fun loginWithSiteOauth2Api(
        @RequestParam wxAppId: String,
        @RequestParam code: String,
        request: HttpServletRequest,
        response: HttpServletResponse
    ): ResponseMessage<Any?> {
        val appSecret = openedAppKeys.wxMiniPrograms[wxAppId]
            ?: throw IllegalArgumentException("Request appId not found.")

        val wxAccessTokenEntity = mpOAuth2Client.getAccessToken(wxAppId, appSecret, code).data
            ?: return ResponseMessage.ANY.error {
                BELLIS_UC_API_ERRORS.GET_WX_OAUTH2_SERVER_ERROR
            }

        loginFacade.wxWebSite(request, wxAppId, wxAccessTokenEntity)

        return ResponseMessage.ANY

    }

}

