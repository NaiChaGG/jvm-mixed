package io.github.jojoti.boundary.utilswx

import io.github.jojoti.commelina.utils.core.kt.domain.DomainEntity
import org.springframework.http.ResponseEntity
import org.springframework.web.client.RestOperations
import org.springframework.web.client.getForEntity
import java.net.URLEncoder

object MPOAuth2Utils {

    fun createWxWebOAuth2Url(
        appId: String,
        redirectUrl: String,
        state: String,
        scope: String = "snsapi_login"
    ): String {
        return "https://open.weixin.qq.com/connect/qrconnect?appid=$appId" +
                "&redirect_uri=${URLEncoder.encode(redirectUrl)}" +
                "&response_type=code&scope=$scope&state=$state#wechat_redirect"
    }

}

interface MPOAuth2Client {

    fun getAccessToken(appId: String, appSecret: String, code: String): DomainEntity<WxAccessTokenEntity?>

}

class MPOAuth2ClientAsRestOp(private val rest: RestOperations) : MPOAuth2Client {

    override fun getAccessToken(appId: String, appSecret: String, code: String): DomainEntity<WxAccessTokenEntity?> {
        val url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=$appId" +
                "&secret=$appSecret&code=$code&grant_type=authorization_code"

        val response: ResponseEntity<WxAccessTokenEntity> = rest.getForEntity(url)

        if (response.statusCode.is2xxSuccessful) {
            val body = response.body
            return when (body) {
                null -> {
                    DomainEntity(null, -1)
                }
                else -> {
                    return if (body.errcode == 0) DomainEntity(body) else DomainEntity(null, -2)
                }
            }
        }
        return DomainEntity(null, -3)
    }

}

data class WxAccessTokenEntity(
    var access_token: String = "",
    var expires_in: Int = 0,
    var refresh_token: String = "",
    var openid: String = "",
    var unionid: String = "",
    var scope: String = "",
    @Transient
    var errcode: Int = 0,
    @Transient
    var errmsg: String = ""
)
