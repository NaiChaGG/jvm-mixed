package io.github.jojoti.sserviceaccount.controller.oauth2

import com.fasterxml.jackson.module.kotlin.readValue
import io.github.jojoti.commelina.webmvc.ResponseBodyMessageData
import io.github.jojoti.commelina.webmvc.session.SessionAttr
import io.github.jojoti.commelina.utils.core.kt.JSONJacksonHolder
import io.github.jojoti.sserviceaccount.common.doamin.AccessTokenEntity
import io.github.jojoti.sserviceaccount.common.doamin.AuthorizeEntity
import junit.framework.TestCase
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext

@RunWith(SpringRunner::class)
@SpringBootTest
@AutoConfigureMockMvc
class OAuth2ControllerTest : SessionAttr {

    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var wac: WebApplicationContext

    @Before
    fun setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build()
    }

    @Test
    fun testOAuth2() {

        mockMvc.perform(
            MockMvcRequestBuilders
                .post("/api/connect/wx/mprogram/code2session")
                .param("wxMiniProgramAppId", "wx28f0fe53b735ce7c")
                .param("code", "021kJ98U1w1pfX0Dq89U1Ozn8U1kJ98y")
        ).andExpect(MockMvcResultMatchers.status().isOk)

        // 构造参数获取oauth2 code
        val authorizeApiRequest = MockMvcRequestBuilders
            .get("/oauth2/authorize/api")
            .accept(MediaType.APPLICATION_JSON)
            .param("appId", "uc1")
            .sessionAttr(this.sessionName(), 2)

        // 获取
        mockMvc.perform(authorizeApiRequest).andExpect(MockMvcResultMatchers.status().isOk)
            .andDo {
                val apiResponse: ResponseBodyMessageData<AuthorizeEntity?> = JSONJacksonHolder.JSON_DEFAULT.readValue(
                    it.response.contentAsString
                )

                val authorizeAccessTokenRequest = MockMvcRequestBuilders
                    .get("/oauth2/access-token")
                    .accept(MediaType.APPLICATION_JSON)
                    .param("appId", "uc1")
                    .param("code", apiResponse.data!!.code)
                    .sessionAttr(this.sessionName(), 2)

                val accessTokenResult = mockMvc.perform(authorizeAccessTokenRequest)
                    .andExpect(MockMvcResultMatchers.status().isOk)
                    .andReturn()

                // 验证 access token 返回值
                val accessTokenResponse: ResponseBodyMessageData<AccessTokenEntity?> =
                    JSONJacksonHolder.JSON_DEFAULT.readValue(accessTokenResult.response.contentAsString)

                TestCase.assertNotNull(accessTokenResponse.data)
                TestCase.assertEquals(accessTokenResponse.data!!.unionId, 2L)

//                    // 构造参数获取oauth2 code
//                    val refreshSessionKeyRequest = MockMvcRequestBuilders
//                            .get("/oauth2/3rd/center/wx-connect-key/refresh")
//                            .accept(MediaType.APPLICATION_JSON)
//                            .param("accessToken", accessTokenResponse.data!!.accessToken)
//                            .param("code", "0710ttTB0o5Udi2N6RUB03GMTB00ttT5")
//                            .sessionAttr(this.sessionName(), 2)
//
//                    mockMvc.perform(refreshSessionKeyRequest)
//                            .andExpect(MockMvcResultMatchers.status().isOk)
//                            .andExpect { refreshSessionKeyIt: MvcResult ->
//                                val refreshSessionKey: ResponseBodyMessageData<RefreshSessionKeyEntity?> =
//                                        JSONJacksonHolder.JSON_DEFAULT.readValue(refreshSessionKeyIt.response.contentAsString)
//                                TestCase.assertNotNull(refreshSessionKey.data)
//                                TestCase.assertTrue(refreshSessionKey.data!!.sessionKey.isNotEmpty())
//                            }

            }.andReturn()
    }


}