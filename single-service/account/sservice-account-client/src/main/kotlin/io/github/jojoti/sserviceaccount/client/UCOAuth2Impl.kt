package io.github.jojoti.sserviceaccount.client

import io.github.jojoti.commelina.webmvc.ResponseBodyMessageData
import io.github.jojoti.commelina.utils.core.kt.domain.DomainEntity
import io.github.jojoti.commelina.utils.core.kt.domain.error
import io.github.jojoti.sserviceaccount.common.doamin.AccessTokenEntity
import io.github.jojoti.sserviceaccount.common.doamin.RefreshSessionKeyEntity
import org.springframework.http.ResponseEntity
import org.springframework.web.client.RestOperations
import org.springframework.web.client.getForEntity
import io.github.jojoti.boundary.utils.RequestUtils;

class UCOAuth2Impl(
    private val ucDomain: String,
    private val appId: String,
    private val appSecret: String,
    private val rest: RestOperations
) : UCOAuth2 {

    override fun getAccessToken(code: String): DomainEntity<AccessTokenEntity?> {
        val urlArg = RequestUtils.createGetUri(
            this.appSecret, mutableMapOf(
                "appId" to this.appId,
                "code" to code
            )
        )

        val url = "$ucDomain/oauth2/access-token?$urlArg"
        val response: ResponseEntity<ResponseBodyMessageData<AccessTokenEntity?>> = this.rest.getForEntity(url)
        val body = response.body
            ?: return DomainEntity.ANY.error { response.statusCodeValue }
        return DomainEntity(body.data)
    }

    override fun refreshWxMiniProgramSessionKey(accessToken: String, wxMiniProgramCode: String)
            : DomainEntity<RefreshSessionKeyEntity?> {
        val urlArg = RequestUtils.createGetUri(
            this.appSecret, mutableMapOf(
                "accessToken" to accessToken,
                "code" to wxMiniProgramCode
            )
        )
        val url = "$ucDomain/oauth2/3rd/center/wx-connect-key/refresh?$urlArg"
        val response: ResponseEntity<ResponseBodyMessageData<RefreshSessionKeyEntity?>> = this.rest.getForEntity(url)
        val body = response.body
            ?: return DomainEntity.ANY.error { response.statusCodeValue }
        return DomainEntity(body.data)
    }

}