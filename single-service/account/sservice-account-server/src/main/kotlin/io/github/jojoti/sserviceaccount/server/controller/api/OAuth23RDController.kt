package io.github.jojoti.sserviceaccount.controller.api

import io.github.jojoti.boundary.opened.client.OpenedAppKeys
import io.github.jojoti.sserviceaccount.cache.TPOAuth2CacheEntity
import io.github.jojoti.sserviceaccount.cache.TPOAuth2CacheRepository
import io.github.jojoti.boundary.utilswx.MPOAuth2Utils
import io.github.jojoti.commelina.utils.core.kt.UnionId
import io.github.jojoti.commelina.webmvc.ResponseMessage
import io.github.jojoti.commelina.webmvc.ok
import io.github.jojoti.commelina.webmvc.session.AllowAnonymousAccess
import com.linkedin.urls.Url
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * oauth登录中控gateway
 * 解决问题， 微信网页部署在本地无法登录问题，因为微信后台只能配置一个oauth2 redirect redirectUrl.
 *
 * 微信后台配置: https://example.com/oauth2/gateway/wx/callback
 * 其它oath2 登录也可以扩展该模式
 */
@RestController
@RequestMapping("/api/3rd/oauth2")
class OAuth23RDController(
    @Value("\${uc.oauth2.gateway.url:}")
    private val siteUrl: String
) {

    @Autowired
    private lateinit var openedAppKeys: OpenedAppKeys

    @Autowired
    private lateinit var oauth2Cache: TPOAuth2CacheRepository

    @PostMapping("/gateway/authorize")
    @AllowAnonymousAccess
    fun createOpenUrl(
        @RequestParam
        thirdPartyType: ThirdPartyType, // 第三方登录类型
        @RequestParam
        thirdPartyAppId: String, // 3rd app id
        @RequestParam
        successRedirectUrl: String, // 授权成功
        @RequestParam
        failedRedirectUrl: String, // 授权失败
        request: HttpServletRequest
    ): ResponseMessage<AuthorizeUrlEntity?> {
        when (thirdPartyType) {
            ThirdPartyType.WEIXIN_OAUTH2 -> {
                openedAppKeys.wxSites[thirdPartyAppId]
                    ?: throw IllegalArgumentException("thirdPartyAppId is not found.")

                val state = UnionId.createUUID()

                oauth2Cache.save(TPOAuth2CacheEntity(state, failedRedirectUrl, successRedirectUrl))

                val redirectUrl = if (siteUrl.isEmpty())
                    request.requestURL.append("/callback").toString() else "$siteUrl/callback"

                return ResponseMessage.ANY.ok {
                    AuthorizeUrlEntity(MPOAuth2Utils.createWxWebOAuth2Url(thirdPartyAppId, redirectUrl, state))
                }
            }
        }
    }

    @GetMapping("/gateway/authorize")
    @AllowAnonymousAccess
    fun wxCreateOpenUrlAutoRedirect(
        @RequestParam
        thirdPartyType: ThirdPartyType, // 第三方登录呃理想
        @RequestParam
        thirdPartyAppId: String, // 3rd app id
        @RequestParam
        // 授权成功
        successRedirectUrl: String,
        @RequestParam
        failedRedirectUrl: String,
        // 授权失败
        request: HttpServletRequest,
        response: HttpServletResponse
    ): ResponseMessage<AuthorizeUrlEntity?>? {
        val responseMessage =
            this.createOpenUrl(thirdPartyType, thirdPartyAppId, successRedirectUrl, failedRedirectUrl, request)
        val data = responseMessage.data ?: return responseMessage
        response.sendRedirect(data.redirectUrl)
        return null
    }

    @GetMapping("/gateway/authorize/callback")
    @AllowAnonymousAccess
    fun wxCallbackRedirect(
        @PathVariable state: String,
        @RequestParam code: String,
        request: HttpServletRequest,
        response: HttpServletResponse
    ) {
        val oauth2Cache = this.oauth2Cache.findById(state).get()
        if (Url.create(oauth2Cache.successUrl).query.isNotEmpty()) {
            response.sendRedirect("${oauth2Cache.successUrl}&oauth2_code=$code")
        } else {
            response.sendRedirect("${oauth2Cache.successUrl}?oauth2_code=$code")
        }
    }
}

data class AuthorizeUrlEntity(val redirectUrl: String)

enum class ThirdPartyType {

    WEIXIN_OAUTH2

}