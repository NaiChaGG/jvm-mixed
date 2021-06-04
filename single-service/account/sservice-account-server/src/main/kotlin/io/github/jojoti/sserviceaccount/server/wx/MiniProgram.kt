package io.github.jojoti.boundary.utilswx

import io.github.jojoti.commelina.utils.core.kt.domain.DomainEntity
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.client.RestOperations
import org.springframework.web.client.getForEntity
import java.io.Serializable

data class MiniProgramCode2SessionEntity(
    var openid: String = "",
    var session_key: String = "",
    var unionid: String = "1",
    @Transient
    var errcode: Int = 0,
    @Transient
    var errmsg: String = ""
) : Serializable

private const val WX_MINI_SESSION_VALID_API = "https://api.weixin.qq.com/sns/jscode2session"

interface MiniProgramAPI {

    fun code2Session(appId: String, appSecret: String, code: String):
            DomainEntity<MiniProgramCode2SessionEntity?>
}

class MiniProgramAPIAsRestOp(private val rest: RestOperations) : MiniProgramAPI {

    companion object {
        private val logger by lazy {
            LoggerFactory.getLogger(MiniProgramAPIAsRestOp::class.java)
        }
    }

    override fun code2Session(appId: String, appSecret: String, code: String):
            DomainEntity<MiniProgramCode2SessionEntity?> {
        val apiUrl = createMiniProgaramCode2SessionUrl(appId, appSecret, code)

        val response: ResponseEntity<MiniProgramCode2SessionEntity> = rest.getForEntity(apiUrl)
        if (logger.isDebugEnabled) {
            logger.debug("weixin api: $WX_MINI_SESSION_VALID_API , response entity {}", response)
        }
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

    private fun createMiniProgaramCode2SessionUrl(appId: String, appSecret: String, code: String): String {
        return WX_MINI_SESSION_VALID_API +
                "?appid=$appId&secret=$appSecret" +
                "&js_code=$code&grant_type=authorization_code"
    }

}