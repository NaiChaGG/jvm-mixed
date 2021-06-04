package io.github.jojoti.sserviceaccount.biz

interface AccountOAuth2 {

    /**
     * oauth2 通用协议
     */
    fun wxRegisterOrCreate(wxAppId: String, openId: String): AccountEntity

}

data class AccountEntity(
    val userId: Long,
    val isNew: Boolean = false
)