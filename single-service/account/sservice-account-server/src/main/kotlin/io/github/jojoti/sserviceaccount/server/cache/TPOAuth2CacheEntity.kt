package io.github.jojoti.sserviceaccount.cache

import org.springframework.data.annotation.Id
import org.springframework.data.redis.core.RedisHash
import org.springframework.data.redis.core.TimeToLive
import javax.persistence.Column

@RedisHash("tp:oauth2")
data class TPOAuth2CacheEntity(
    @Id
    @Column
    val state: String,
    val failedUrl: String,
    val successUrl: String,
    @TimeToLive
    val timeout: Long = 1800
)