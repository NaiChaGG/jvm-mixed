package io.github.jojoti.boundary.utilswx

import com.fasterxml.jackson.module.kotlin.readValue
import com.google.common.io.BaseEncoding
import io.github.jojoti.commelina.utils.core.kt.JSONJacksonHolder
import org.bouncycastle.jce.provider.BouncyCastleProvider
import java.nio.charset.Charset
import java.security.AlgorithmParameters
import java.security.InvalidAlgorithmParameterException
import java.security.NoSuchProviderException
import java.security.Security
import java.util.*
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

object MiniProgramDataDecoder {

    fun decodeUserData(sessionKey: String, data: String, iv: String): WxUserDataEntity {
        return wxMiniDataDecrypt(sessionKey, data, iv)
    }

}

data class WxUserDataEntity(
    val openId: String,
    val userName: String,
    val userAvatarUrl: String
)

class WxAESDecodeException(cause: Throwable?) : RuntimeException(cause)

/**
 *
 * 封装对外访问方法
 * 解密数据
 *
 * @return
 * @throws Exception
 */
private inline fun <reified T> wxMiniDataDecrypt(sessionKey: String, encryptedData: String, iv: String): T {

    try {
        val resultByte = MyAES.DEFAULT.decrypt(
            BaseEncoding.base64().decode(encryptedData),
            BaseEncoding.base64().decode(sessionKey),
            BaseEncoding.base64().decode(iv)
        ) ?: throw IllegalArgumentException("Decode error.")

        if (resultByte.isEmpty()) {
            throw IllegalArgumentException("Decode error.")
        }

        return JSONJacksonHolder.JSON_DEFAULT.readValue(WxPKCS7Encoder.decode(resultByte))
    } catch (e: Throwable) {
        throw  WxAESDecodeException(e)
    }
}

/**
 * copy by https://bijian1013.iteye.com/blog/2427653
 */
private class MyAES {

    /**
     * AES解密
     *
     * @param content
     * 密文
     * @return
     * @throws InvalidAlgorithmParameterException
     * @throws NoSuchProviderException
     */
    fun decrypt(content: ByteArray, keyByte: ByteArray, ivByte: ByteArray): ByteArray? {
        val cipher = Cipher.getInstance("AES/CBC/PKCS7Padding")
        val sKeySpec = SecretKeySpec(keyByte, "AES")
        cipher.init(Cipher.DECRYPT_MODE, sKeySpec, generateIV(ivByte))// 初始化
        return cipher.doFinal(content)
    }

    /**
     * 生成iv
     */
    private fun generateIV(iv: ByteArray): AlgorithmParameters {
        val params = AlgorithmParameters.getInstance("AES")
        params.init(IvParameterSpec(iv))
        return params
    }

    companion object {
        val DEFAULT by lazy {
            Security.addProvider(BouncyCastleProvider())
            MyAES()
        }
    }

}

/**
 * 微信小程序加解密
 */
private object WxPKCS7Encoder {

    private const val BLOCK_SIZE = 32

    private val CHARSET = Charset.forName("utf-8")

    /**
     * 获得对明文进行补位填充的字节.
     *
     * @param count
     * 需要进行填充补位操作的明文字节个数
     * @return 补齐用的字节数组
     */
    fun encode(count: Int): ByteArray {
        // 计算需要填充的位数
        var amountToPad = BLOCK_SIZE - count % BLOCK_SIZE
        if (amountToPad == 0) {
            amountToPad = BLOCK_SIZE
        }
        // 获得补位所用的字符
        val padChr = chr(amountToPad)
        var tmp = String()
        for (index in 0 until amountToPad) {
            tmp += padChr
        }
        return tmp.toByteArray(CHARSET)
    }

    /**
     * 删除解密后明文的补位字符
     *
     * @param decrypted
     * 解密后的明文
     * @return 删除补位字符后的明文
     */
    fun decode(decrypted: ByteArray): ByteArray {
        var pad = decrypted[decrypted.size - 1].toInt()
        if (pad < 1 || pad > 32) {
            pad = 0
        }
        return Arrays.copyOfRange(decrypted, 0, decrypted.size - pad)
    }

    /**
     * 将数字转化成ASCII码对应的字符，用于对明文进行补码
     *
     * @param a
     * 需要转化的数字
     * @return 转化得到的字符
     */
    private fun chr(a: Int): Char {
        val target = (a and 0xFF).toByte()
        return target.toChar()
    }
}