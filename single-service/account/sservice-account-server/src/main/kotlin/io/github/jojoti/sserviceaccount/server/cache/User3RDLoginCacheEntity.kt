package io.github.jojoti.sserviceaccount.cache

import io.github.jojoti.sserviceaccount.common.doamin.LoginType
import org.springframework.data.annotation.Id
import org.springframework.data.redis.core.RedisHash
import org.springframework.data.redis.core.TimeToLive
import java.io.Serializable

/**
 * 用户登录第三方账号的时候缓存的登录信息
 */
@RedisHash(value = "user:3rd:login")
data class User3RDLoginEntity(
    val userId: Long,
    val loginType: LoginType,
    val appId: String,
    val openId: String,
    var accessToken: String,                // 小程序这里就是session key， refresh token 为空
    val unionId: String = "",               // wx特有
    val refreshToken: String = "",          // OAuth2 刷新 token
    var refreshTokenExpireTime: Long = 0,   // OAuth2 过期时间
    @TimeToLive
    val timeout: Long = -1, // 永不过期
    val refreshTokenLastTime: Long = System.currentTimeMillis(),
    @Id
    val oauth2Id: String = User3RDLoginId.gen(userId, loginType, appId)
) : Serializable

object User3RDLoginId {

    fun gen(userId: Long, loginType: LoginType, appId: String): String {
        return "$userId:$loginType:$appId"
    }

}


