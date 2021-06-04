package io.github.jojoti.sserviceaccount.controller.oauth2

import com.google.common.base.Joiner
import io.github.jojoti.boundary.opened.client.AppProvider
import io.github.jojoti.boundary.opened.client.Opened
import io.github.jojoti.sserviceaccount.proto.BELLIS_UC_API_ERRORS
import io.github.jojoti.commelina.webmvc.MainFlowInterceptor
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.web.method.HandlerMethod
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class OAuth2AccessInterceptor : MainFlowInterceptor() {

    @Autowired
    private lateinit var opened: Opened

    companion object {
        private val MY_LOGGER by lazy {
            LoggerFactory.getLogger(OAuth2AccessInterceptor::class.java)
        }
    }

    override fun doPreHandler(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: HandlerMethod
    ): Boolean {
        if (MY_LOGGER.isDebugEnabled) {
            return true
        }

        val signArgs = mutableMapOf<String, String>()
        var sign = ""
        var appId = ""
        var time = 0L

        request.parameterMap.forEach { k, v ->
            when (k) {
                "sign" -> {
                    sign = v[0]
                }
                "appId" -> {
                    appId = v[0]
                    signArgs[k] = appId
                }
                "_time" -> {
                    val timeStr = v[0]
                    time = timeStr.toLong()
                    signArgs[k] = timeStr
                }
                else -> {
                    signArgs[k] = Joiner.on("").join(v)
                }
            }
        }

        if (System.currentTimeMillis() - time > 100000) {
            return this.createResponse(response, BELLIS_UC_API_ERRORS.OAUTH2_APP_SIGN_TIME_EXPIRED_VALUE)
        }

        if (!(opened.validSign(AppProvider.UC, appId, sign, signArgs).data
                ?: return this.createResponse(response, BELLIS_UC_API_ERRORS.OAUTH2_APP_ID_SIGN_ERROR_VALUE))
        ) {
            return false
        }

        return super.doPreHandler(request, response, handler)
    }

}