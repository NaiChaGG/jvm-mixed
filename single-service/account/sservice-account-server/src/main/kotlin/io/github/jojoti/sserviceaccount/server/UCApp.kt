package io.github.jojoti.sserviceaccount

import io.github.jojoti.boundary.opened.client.AppProvider
import io.github.jojoti.boundary.opened.client.Opened
import io.github.jojoti.boundary.utilswx.MPOAuth2Client
import io.github.jojoti.boundary.utilswx.MPOAuth2ClientAsRestOp
import io.github.jojoti.boundary.utilswx.MiniProgramAPI
import io.github.jojoti.boundary.utilswx.MiniProgramAPIAsRestOp
import io.github.jojoti.commelina.webmvc.ArgSignInterceptor
import io.github.jojoti.commelina.webmvc.ArgsSign
import io.github.jojoti.commelina.webmvc.session.AuthSessionInterceptor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer
import org.springframework.http.MediaType
import org.springframework.http.client.ClientHttpRequestFactory
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@SpringBootApplication
@EnableUCServer
class UCApp {

    @Bean
    @Qualifier("appOAuthArgSignInterceptor")
    fun createAppOAuthArgSignInterceptor(): ArgSignInterceptor {
        return ArgSignInterceptor(@Component object : ArgsSign {
            @Autowired
            private lateinit var opened: Opened

            override fun doSign(sign: String, args: Map<String, String>): Boolean {
                val appId = args["appId"] ?: throw IllegalArgumentException("Arg app id not found.")
                return opened.validSign(AppProvider.UC, appId, sign, args).data ?: return false
            }
        })
    }

    @Bean
    fun webMvcConfigurer(
        authSessionInterceptor: AuthSessionInterceptor,
        @Qualifier
        appApiArgSignInterceptor: ArgSignInterceptor,
        @Qualifier("appOAuthArgSignInterceptor")
        appOAuthArgSignInterceptor: ArgSignInterceptor

    ): WebMvcConfigurer {
        return object : WebMvcConfigurer {
            override fun addInterceptors(registry: InterceptorRegistry) {

                // 签名拦截器
                registry.addInterceptor(appApiArgSignInterceptor)
                    .addPathPatterns("/api", "/api/**")

                // OAuth2签名拦截器
                registry.addInterceptor(appOAuthArgSignInterceptor)
                    .addPathPatterns("/oauth2", "/oauth2/**")

                // 会话拦截器
                registry.addInterceptor(authSessionInterceptor)
                    .addPathPatterns("/api", "/api/**")
                    .addPathPatterns("/oauth2", "/oauth2/**")
                    .addPathPatterns("/admin", "/admin/**")

            }

            // 跨域配置
            override fun addCorsMappings(registry: CorsRegistry) {
                registry.addMapping("/**")
                    .allowedOrigins("*")
                    .allowedHeaders("x-requested-with", "content-type")
                    .allowedMethods("GET", "POST")
            }

        }
    }

    @Bean
    fun placeholderConfigurer(): PropertySourcesPlaceholderConfigurer {
        val c = PropertySourcesPlaceholderConfigurer()
        // 配置改bean 支持 spring boot yml 多文件 @Value 无法读取
        c.setIgnoreUnresolvablePlaceholders(true)
        return c
    }

    @Bean
    fun restTemplate(factory: ClientHttpRequestFactory): RestTemplate {
        val rest = RestTemplate(factory)
        rest.messageConverters = listOf(object : MappingJackson2HttpMessageConverter() {
            // 任何 mdia 都用json解析
            override fun canRead(clazz: Class<*>, mediaType: MediaType?): Boolean {
                return true
            }
        })
        return rest
    }

    @Bean
    fun simpleClientHttpRequestFactory(): ClientHttpRequestFactory {
        val factory = HttpComponentsClientHttpRequestFactory()
        factory.setReadTimeout(5000)//ms
        factory.setConnectTimeout(15000)//ms
        return factory
    }

    /**
     * wx lib beans
     * ---------------
     */
    @Bean
    fun miniProgramAPI(restTemplate: RestTemplate): MiniProgramAPI {
        return MiniProgramAPIAsRestOp(restTemplate)
    }

    @Bean
    fun mpOAuth2Client(restTemplate: RestTemplate): MPOAuth2Client {
        return MPOAuth2ClientAsRestOp(restTemplate)
    }

}

fun main(args: Array<String>) {

//    runApplication<UCApp>(*args)
//
    SpringApplicationBuilder(UCApp::class.java)
        .properties("spring.uc.name=uc-server").run(*args)

}