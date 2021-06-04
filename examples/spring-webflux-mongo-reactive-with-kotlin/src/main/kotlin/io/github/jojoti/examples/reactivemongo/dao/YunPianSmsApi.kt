package io.github.jojoti.examples.reactivemongo.dao

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.reactive.function.BodyInserters.fromFormData
import org.springframework.web.reactive.function.client.ClientResponse
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

/**
 * @link https://www.yunpian.com/doc/zh_CN/introduction/brief.html
 */
@Component
class YunPianSmsApi : SmsApiDao {

    private val singleSendURL: String = "https://sms.yunpian.com/v2/sms/single_send.json"

    @Value("\${sms.yunpianApiKey}")
    lateinit var apiKey: String

    private val singleSendWebClient = WebClient.create(singleSendURL)

    private val logger: Logger = LoggerFactory.getLogger(YunPianSmsApi::class.java)

    /**
     * https://www.yunpian.com/doc/zh_CN/domestic/single_send.html
     */
    override fun send(telephone: String, content: String): Mono<Boolean> {
        val fromData = LinkedMultiValueMap<String, String>()
        fromData.set("apikey", apiKey)
        fromData.set("mobile", telephone)
        fromData.set("text", content)

        return singleSendWebClient.post()
            .body(fromFormData(fromData))
            .exchange()
            .doOnError { e: Throwable ->
                logger.error("Yun pian send single response error {}", e)
                Mono.just(false)
            }
            .flatMap { t: ClientResponse ->
                logger.debug("Yun pian send single response {}", t)
                Mono.just(true)
            }.switchIfEmpty(Mono.just(false))
    }

}