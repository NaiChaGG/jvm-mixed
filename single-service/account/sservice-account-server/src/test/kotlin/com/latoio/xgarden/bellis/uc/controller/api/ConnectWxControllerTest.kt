package io.github.jojoti.sserviceaccount.controller.api

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext

@RunWith(SpringRunner::class)
@SpringBootTest
@AutoConfigureMockMvc
class ConnectWxControllerTest {

    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var wac: WebApplicationContext

    @Before
    fun setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build()
    }

    @Test
    fun loginWithMiniProgram() {
        mockMvc.perform(
            MockMvcRequestBuilders
                .post("/api/connect/wx/mprogram/code2session")
                .param("wxMiniProgramAppId", "wx28f0fe53b735ce7c")
                .param("code", "071BK6p90NnSPx1zsvo90cmep90BK6pi")
        ).andExpect(MockMvcResultMatchers.status().isOk)

    }
}