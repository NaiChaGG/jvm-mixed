package io.github.jojoti.sserviceaccount.biz

interface AccountWxOAuth2 : AccountOAuth2 {

    /**
     * 微信认证登录
     */
    fun wxRegisterOrCreate(wxAppId: String, openId: String, unionId: String): AccountEntity

}