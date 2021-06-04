package io.github.jojoti.sserviceaccount.controller.oauth2

import io.github.jojoti.boundary.opened.client.AppProvider
import io.github.jojoti.boundary.opened.client.OAuthScope
import io.github.jojoti.boundary.opened.client.Opened
import io.github.jojoti.boundary.opened.client.OpenedOAuth2
import io.github.jojoti.sserviceaccount.cache.OAuth2CacheEntity
import io.github.jojoti.sserviceaccount.cache.OAuth2CacheRepository
import io.github.jojoti.sserviceaccount.cache.User3RDLoginCacheRepository
import io.github.jojoti.sserviceaccount.cache.User3RDLoginId
import io.github.jojoti.sserviceaccount.common.doamin.AccessTokenEntity
import io.github.jojoti.sserviceaccount.common.doamin.AuthorizeEntity
import io.github.jojoti.sserviceaccount.common.doamin.LoginType
import io.github.jojoti.sserviceaccount.proto.BELLIS_UC_API_ERRORS
import io.github.jojoti.commelina.webmvc.ResponseMessage
import io.github.jojoti.commelina.webmvc.error
import io.github.jojoti.commelina.webmvc.ok
import io.github.jojoti.commelina.webmvc.session.AllowAnonymousAccess
import io.github.jojoti.commelina.webmvc.session.AuthSession
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping("/oauth2")
class OAuth2Controller {

    @Autowired
    private lateinit var openedOAuth2: OpenedOAuth2

    @Autowired
    private lateinit var opened: Opened

    @Autowired
    private lateinit var authSession: AuthSession

    @Autowired
    private lateinit var oauth2Cache: OAuth2CacheRepository

    @Autowired
    private lateinit var oauth2UserCreateUser3RDLoginCache: User3RDLoginCacheRepository

    /**
     * @step 1
     *
     * fixme tbd redirectUrl redirect 那一套
     *
     * web api
     */
    @GetMapping("/authorize")
    fun authorize(
        @RequestParam appId: String,
        @RequestParam scope: String,
        request: HttpServletRequest
    ) {

    }

    /**
     * @step 1
     *
     * client api
     */
    @GetMapping("/authorize/api")
    fun authorizeApi(
        @RequestParam appId: String,
        request: HttpServletRequest
    ): ResponseMessage<AuthorizeEntity?> {

        opened.getAppKey(AppProvider.UC, appId).data
            ?: return ResponseMessage.ANY.error { BELLIS_UC_API_ERRORS.OAUTH2_APP_ID_NOT_FOUND }

        val loginUserId = authSession.getJoinUserId(request)

        val domainEntity = openedOAuth2.createNew(
            appId,
            loginUserId,
            OAuthScope.LOGIN,
            OAuthScope.REFRESH_WX_MINI_PROGRAM_KEY
        )

        val type = request.session.getAttribute("loginType") as LoginType
        val loginAppId = request.session.getAttribute("loginTPAppId") as String

        // 缓存登录信息
        this.oauth2Cache.save(OAuth2CacheEntity(domainEntity.code, type, loginUserId, loginAppId))

        return ResponseMessage.ANY.ok { AuthorizeEntity(domainEntity.code) }
    }

    /**
     * @step 2
     * 外部服务器通过 code 查询用户信息, 返回 accessToken
     *
     * @response {
     *      accessToken:(string),         // uc access token
     *      expiresIn:(string),           // uc refresh token ttl
     *      refreshToken:(string),        // uc refresh token
     *
     *      thirdPartyType: (enum),       // 目前只有微信小程序和微信网页登录，uc account
     *      thirdPartyToken: (string),    // 3rd返回的 access token／ connect key / uc-account 话该值为空
     *      openId: (long),               // 当前 app id 唯一
     *      unionId: (long),              // 全局唯一的 userId
     * }
     */
    @GetMapping("/access-token")
    @AllowAnonymousAccess
    fun accessToken(
        @RequestParam appId: String,
        @RequestParam code: String,
        request: HttpServletRequest
    ): ResponseMessage<AccessTokenEntity?> {
        // 根据 code 创建access token
        val accessToken = openedOAuth2.createAccessToken(appId, code).data
            ?: return ResponseMessage.ANY.error { BELLIS_UC_API_ERRORS.OAUTH2_CODE_EXPIRED }

        // 找到当前oauth 的登录信息
        val oauthCacheData = this.oauth2Cache.findById(code)
        if (!oauthCacheData.isPresent) {
            return ResponseMessage.ANY.error { BELLIS_UC_API_ERRORS.UC_USER_SESSION_EXPIRED }
        }

        val currentData = oauthCacheData.get()

        this.oauth2Cache.deleteById(code)

        val loginDataOp = this.oauth2UserCreateUser3RDLoginCache.findById(
            User3RDLoginId.gen(currentData.userId, currentData.loginType, currentData.appId)
        )

        // 登录信息不存在，则需要提示用户重新登录
        if (!loginDataOp.isPresent) {
            return ResponseMessage.ANY.error { BELLIS_UC_API_ERRORS.UC_USER_SESSION_EXPIRED }
        }

        val loginData = loginDataOp.get()

        // 保存新的 oauth info
        this.oauth2Cache.save(
            OAuth2CacheEntity(
                accessToken.accessToken,
                currentData.loginType,
                currentData.userId,
                currentData.appId,
                accessToken.expiresIn.toLong()
            )
        )

        return ResponseMessage.ANY.ok {
            AccessTokenEntity(
                accessToken.accessToken,
                accessToken.expiresIn,
                accessToken.refreshToken,
                accessToken.openId,
                accessToken.unionId,
                loginData.loginType,
                loginData.accessToken
            )
        }

    }

    /**
     * @step 3
     * @response {
     *      accessToken:(string),
     *      expiresIn:(string),
     *      refreshToken:(string),
     * }
     *
     * fixme tbd 现在 access token 永不过期，一个用户一个
     */
    @GetMapping("/refresh-token")
    @AllowAnonymousAccess
    fun refreshToken(
        @RequestParam refreshToken: String,
        @RequestParam sign: String
    ) {
    }

}

