package io.github.jojoti.examples.reactivemongo.biz

import io.github.jojoti.examples.reactivemongo.dao.AccountRepository
import io.github.jojoti.examples.reactivemongo.dao.MemberRepository
import io.github.jojoti.examples.reactivemongo.dto.MemberDto
import io.github.jojoti.examples.reactivemongo.dto.MemberOrNewDto
import io.github.jojoti.examples.reactivemongo.entity.AccountEntity
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class MemberBizImpl : MemberBiz {

    @Autowired
    private lateinit var smsCaptcha: SmsCaptchaBiz

    @Autowired
    private lateinit var account: AccountRepository

    @Autowired
    private lateinit var member: MemberRepository

    @Autowired
    private lateinit var accountBiz: AccountBiz

    companion object {
        private val LOGGER: Logger = LoggerFactory.getLogger(MemberBizImpl::class.java)
        private val ERROR_SMS_CODE_VERIFY_ERROR: MemberOrNewDto =
            MemberOrNewDto(ERROR_CODE.LOGIN_TELEPHONE_SMS_CODE_VALIED_FAILED)
        private val ERROR_ACCOUNT_OR_PWD_ERROR_FOR_MEMBER: MemberDto = MemberDto(ERROR_CODE.LOGIN_ACCOUNT_PWD_ERROR)
    }

    override fun signInByMixedAccount(mixedAccount: String, pwd: String): Mono<MemberDto> {
        return accountPwdVerify(account.findByMixed(mixedAccount), pwd)
    }

    override fun signInByEmailAccount(email: String, pwd: String): Mono<MemberDto> {
        return accountPwdVerify(account.findByEmail(email), pwd)
    }

    override fun signInByTelephoneAccount(telephone: String, pwd: String): Mono<MemberDto> {
        return accountPwdVerify(account.findByTelephone(telephone), pwd)
    }

    /**
     * 手机号根据短信验证码登录
     */
    override fun signInByTelephoneAccountNoPass(telephone: String, smsCode: String): Mono<MemberOrNewDto> {
        return smsCaptcha.verify(telephone, smsCode)
            .flatMap {
                if (it) this.findOrCreateTelephoneNoPassUser(telephone)
                else Mono.just(ERROR_SMS_CODE_VERIFY_ERROR)
            }
    }

    /**
     * 验证账号密码并返回用户 entity
     */
    private fun accountPwdVerify(monoAccount: Mono<AccountEntity>, pwd: String): Mono<MemberDto> {
        return monoAccount.flatMap { account ->
            member.findById(account.uid)
                .flatMap {
                    // 验证用户密码是否正确
                    if (PasswordAuthentication.getDefault().authenticate(pwd.toCharArray(), it.pwd))
                        Mono.just(MemberDto(CORE_ERROR_CODE.SUCESS, it.uid, account.account))
                    else
                        Mono.just(ERROR_ACCOUNT_OR_PWD_ERROR_FOR_MEMBER)
                }
                .switchIfEmpty(
                    Mono.just(ERROR_ACCOUNT_OR_PWD_ERROR_FOR_MEMBER)
                )
                .doOnNext {
                    LOGGER.error("error result:{}", it)
                }
        }.switchIfEmpty(Mono.just(ERROR_ACCOUNT_OR_PWD_ERROR_FOR_MEMBER))

    }

    /**
     * 查询或者创建手机号免密码登录用户
     */
    private fun findOrCreateTelephoneNoPassUser(telephone: String): Mono<MemberOrNewDto> {
        // 1 验证该手机账户是否为手机账户密码登陆
        return account.findByTelephoneNoPass(telephone)
            .flatMap {
                member.findById(it.uid)
                    .flatMap {
                        Mono.just(MemberOrNewDto(CORE_ERROR_CODE.SUCESS, it.uid, telephone))
                    }
                    .switchIfEmpty(
                        // member 不存在，这里是脏数据
                        account.deleteById(it.objectId).flatMap {
                            LOGGER.error("Never used delete account.")
                            this.createAccount(telephone)
                        })
                    .doOnError {
                        LOGGER.error("Never used.")
                    }
            }
            .switchIfEmpty(this.createAccount(telephone))
    }

    private fun createAccount(telephone: String): Mono<MemberOrNewDto> {
        return accountBiz.createNewTelephoneNoPassAccount(telephone)
            .flatMap {
                if (it.isSuccess)
                    Mono.just(MemberOrNewDto(CORE_ERROR_CODE.SUCESS, it.uid, telephone))
                else
                    Mono.just(MemberOrNewDto(it.errorCode))
            }
    }

}