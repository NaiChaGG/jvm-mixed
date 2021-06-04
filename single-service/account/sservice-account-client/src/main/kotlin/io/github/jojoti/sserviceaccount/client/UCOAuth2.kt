package io.github.jojoti.sserviceaccount.client

import io.github.jojoti.commelina.utils.core.kt.domain.DomainEntity
import io.github.jojoti.sserviceaccount.common.doamin.AccessTokenEntity
import io.github.jojoti.sserviceaccount.common.doamin.RefreshSessionKeyEntity

interface UCOAuth2 {

    fun getAccessToken(code: String): DomainEntity<AccessTokenEntity?>

    fun refreshWxMiniProgramSessionKey(
        accessToken: String,
        wxMiniProgramCode: String
    ): DomainEntity<RefreshSessionKeyEntity?>

}