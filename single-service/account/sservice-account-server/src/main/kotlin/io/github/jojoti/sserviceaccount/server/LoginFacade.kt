package io.github.jojoti.sserviceaccount

import io.github.jojoti.sserviceaccount.biz.AccountEntity
import io.github.jojoti.sserviceaccount.biz.AccountWxOAuth2
import io.github.jojoti.sserviceaccount.cache.User3RDLoginCacheRepository
import io.github.jojoti.sserviceaccount.cache.User3RDLoginEntity
import io.github.jojoti.boundary.utilswx.MiniProgramCode2SessionEntity
import io.github.jojoti.boundary.utilswx.WxAccessTokenEntity
import io.github.jojoti.commelina.webmvc.session.AuthSession
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import javax.servlet.http.HttpServletRequest

@Component
class LoginFacade {

    @Autowired
    private lateinit var accountWxOAuth2: AccountWxOAuth2

    @Autowired
    private lateinit var authSession: AuthSession

    @Autowired
    private lateinit var user3RDLoginCache: User3RDLoginCacheRepository

    fun wxMiniProgram(request: HttpServletRequest, appId: String, wxUser: MiniProgramCode2SessionEntity) {
        val account = this.wxLogin(request, appId, wxUser.openid, wxUser.unionid)

        // 缓存第三方登录信息
        user3RDLoginCache.save(
            User3RDLoginEntity(
                account.userId,
                LoginType.WX_MINI_PROGRAM,
                appId,
                wxUser.openid,
                wxUser.session_key,
                wxUser.unionid
            )
        )

        // 登录的类型
        request.session.setAttribute("loginType", LoginType.WX_MINI_PROGRAM)
        // 第三方server的appId
        request.session.setAttribute("loginTPAppId", appId)
    }

    fun wxWebSite(request: HttpServletRequest, appId: String, wxUser: WxAccessTokenEntity) {
        val account = this.wxLogin(request, appId, wxUser.openid, wxUser.unionid)

        // 缓存第三方登录信息
        user3RDLoginCache.save(
            User3RDLoginEntity(
                account.userId,
                LoginType.WX_MINI_PROGRAM,
                appId,
                wxUser.openid,
                wxUser.access_token,
                wxUser.unionid,
                wxUser.refresh_token,
                wxUser.expires_in * 1000 + System.currentTimeMillis(),
                wxUser.expires_in.toLong() // 缓存过期时间
            )
        )

        // 登录的类型
        request.session.setAttribute("loginType", LoginType.WX_WEB_SITE_OAUTH2)
        // 登录的类型
        request.session.setAttribute("loginTPAppId", appId)
    }

    private fun wxLogin(
        request: HttpServletRequest,
        appId: String,
        openId: String,
        unionId: String
    ): AccountEntity {
        val accountCreateEntity = when (unionId) {
            "" -> {
                accountWxOAuth2.wxRegisterOrCreate(appId, openId)
            }
            else -> {
                accountWxOAuth2.wxRegisterOrCreate(appId, openId, unionId)
            }
        }

        authSession.join(request, accountCreateEntity.userId)

        return accountCreateEntity
    }

}