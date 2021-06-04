package io.github.jojoti.sserviceaccount

data class AccessTokenEntity(
    val accessToken: String,
    val expiresIn: Int,
    val refreshToken: String,
    val openId: Long,
    val unionId: Long,
    val thirdPartyType: LoginType,
    var thirdPartyToken: String
)