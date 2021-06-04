package io.github.jojoti.sserviceaccount.controller.oauth2

import io.github.jojoti.boundary.opened.client.OAuthScope
import io.github.jojoti.boundary.opened.client.OpenedAppKeys
import io.github.jojoti.boundary.opened.client.OpenedOAuth2
import io.github.jojoti.sserviceaccount.cache.OAuth2CacheRepository
import io.github.jojoti.sserviceaccount.cache.User3RDLoginCacheRepository
import io.github.jojoti.sserviceaccount.cache.User3RDLoginId
import io.github.jojoti.sserviceaccount.common.doamin.RefreshSessionKeyEntity
import io.github.jojoti.sserviceaccount.proto.BELLIS_UC_API_ERRORS
import io.github.jojoti.boundary.utilswx.MiniProgramAPI
import io.github.jojoti.commelina.webmvc.ResponseMessage
import io.github.jojoti.commelina.webmvc.error
import io.github.jojoti.commelina.webmvc.ok
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

/**
 * 第三方登录中控刷新
 */
@RestController
@RequestMapping("/openedOAuth2/3rd/center")
class OAuth23RDCenterController {

    @Autowired
    private lateinit var openedAppKeys: OpenedAppKeys

    @Autowired
    private lateinit var miniProgramAPI: MiniProgramAPI

    @Autowired
    private lateinit var openedOAuth2: OpenedOAuth2

    @Autowired
    private lateinit var oauth2Cache: OAuth2CacheRepository

    @Autowired
    private lateinit var oauth2UserCreateUser3RDLoginCache: User3RDLoginCacheRepository

    /**
     * 更新小程序session-key
     */
    @GetMapping("/wx-connect-key/refresh")
    fun refreshWxMiniProgramKey(
        @RequestParam accessToken: String,
        @RequestParam code: String
    ): ResponseMessage<RefreshSessionKeyEntity?> {

        val accessData = openedOAuth2.getUserId(accessToken, OAuthScope.REFRESH_WX_MINI_PROGRAM_KEY).data
            ?: return ResponseMessage.ANY.error { BELLIS_UC_API_ERRORS.UC_ACCESS_TOKEN_VALID_FAILED }

        val oauth2CacheDataOp = oauth2Cache.findById(accessToken)
        if (!oauth2CacheDataOp.isPresent) {
            return ResponseMessage.ANY.error { BELLIS_UC_API_ERRORS.UC_USER_SESSION_EXPIRED }
        }

        val oauth2CacheData = oauth2CacheDataOp.get()

        val appSecret = openedAppKeys.wxMiniPrograms[oauth2CacheData.appId]
            ?: throw IllegalArgumentException("AppId :{${oauth2CacheData.appId}} not found.")

        val entity = miniProgramAPI.code2Session(oauth2CacheData.appId, appSecret, code)

        val code2SessionData = entity.data
            ?: return ResponseMessage.ANY.error { BELLIS_UC_API_ERRORS.GET_WX_MINI_PROGRAM_SERVER_ERROR }

        val loginCachedDataOP = this.oauth2UserCreateUser3RDLoginCache.findById(
            User3RDLoginId.gen(
                oauth2CacheData.userId,
                oauth2CacheData.loginType,
                oauth2CacheData.appId
            )
        )

        if (!loginCachedDataOP.isPresent) {
            return ResponseMessage.ANY.error { BELLIS_UC_API_ERRORS.UC_USER_SESSION_EXPIRED }
        }

        val loginCachedData = loginCachedDataOP.get()

        loginCachedData.accessToken = code2SessionData.session_key

        // 更新 connect key 缓存
        this.oauth2UserCreateUser3RDLoginCache.save(loginCachedData)

        return ResponseMessage.ANY.ok { RefreshSessionKeyEntity(code2SessionData.session_key) }
    }

    /**
     * 更新第三方 access token
     * fixme tbd 目前只有登录，所以这个接口没啥用
     */
    @GetMapping("/wx-openedOAuth2/refresh")
    fun refreshWxOauth2(
        @RequestParam accessToken: String
    ) {

    }

}

