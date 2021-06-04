package io.github.jojoti.sserviceaccount.cache

import io.github.jojoti.sserviceaccount.common.doamin.LoginType
import org.springframework.data.annotation.Id
import org.springframework.data.redis.core.RedisHash
import org.springframework.data.redis.core.TimeToLive
import java.io.Serializable

/**
 * 用户登录第三方账号的时候缓存的登录信息
 */
@RedisHash(value = "oauth2")
data class OAuth2CacheEntity(
    @Id
    val oauth2Id: String,
    val loginType: LoginType,
    val userId: Long,
    val appId: String,
    @TimeToLive
    val timeout: Long = 180

) : Serializable



