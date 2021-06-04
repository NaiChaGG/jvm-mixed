package io.github.jojoti.examples.reactivemongo.biz

import io.github.jojoti.examples.reactivemongo.dao.AccountRepository
import io.github.jojoti.examples.reactivemongo.dao.MemberRepository
import io.github.jojoti.examples.reactivemongo.dto.MemberDto
import io.github.jojoti.examples.reactivemongo.entity.AccountEntity
import io.github.jojoti.examples.reactivemongo.entity.MemberEntity
import io.github.jojoti.examples.reactivemongo.proto.ERROR_CODE
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

import org.jasypt.util.password.rfc2307.RFC2307SSHAPasswordEncryptor


@Service
class AccountBizImpl : AccountBiz {

    @Autowired
    private lateinit var account: AccountRepository

    @Autowired
    private lateinit var member: MemberRepository

    // 伴生对象
    companion object {
        private val ERROR_TELEPHONE_EXISTS: MemberDto = MemberDto(ERROR_CODE.LOGIN_TELEPHONE_EXISTS)

        private val ACCOUNT_EXISTS: MemberDto = MemberDto(ERROR_CODE.ACCOUNT_EXISTS)

        private val LOGGER: Logger = LoggerFactory.getLogger(AccountBizImpl::class.java)

        const val TELEPHONE_ACCOUNT_PWD_LAST: Int = 4

        // 执行时间大约 5ms
        val PASSWORD_ENCRYPTOR: RFC2307SSHAPasswordEncryptor = RFC2307SSHAPasswordEncryptor()

    }

    override fun createNewMixedAccount(mixed: String, password: String): Mono<MemberDto> {
        return account.accountExists(mixed)
            .flatMap {
                Mono.just(ACCOUNT_EXISTS)
            }.switchIfEmpty(this.createMember(mixed, password, AccountEntity.AccountType.MIXED))
    }

    override fun createNewEmailAccount(email: String, password: String): Mono<MemberDto> {
        return account.accountExists(email)
            .flatMap {
                Mono.just(ACCOUNT_EXISTS)
            }.switchIfEmpty(this.createMember(email, password, AccountEntity.AccountType.EMAIL))
    }

    override fun createNewTelephoneAccount(telephone: String, password: String): Mono<MemberDto> {
        return account.accountExists(telephone)
            .flatMap {
                Mono.just(ACCOUNT_EXISTS)
            }.switchIfEmpty(this.createMember(telephone, password, AccountEntity.AccountType.TELEPHONE))
    }

    override fun createNewTelephoneNoPassAccount(telephone: String): Mono<MemberDto> {
        return account.accountExists(telephone)
            .flatMap {
                Mono.just(ERROR_TELEPHONE_EXISTS)
            }.switchIfEmpty(
                this.createMember(
                    telephone,
                    getTelephonePwd(telephone),
                    AccountEntity.AccountType.TELEPHONE_NO_PASS
                )
            )
    }

    /**
     * 创建用户
     */
    private fun createMember(account: String, pwd: String, accountType: AccountEntity.AccountType): Mono<MemberDto> {
        return member.save(MemberEntity(PASSWORD_ENCRYPTOR.encryptPassword(pwd)))
            .flatMap {
                this.account.save(AccountEntity(account, it.uid, accountType))
                    .doOnError { error: Throwable ->
                        // 如何抛出了异常，则看是不是数据库唯一索引的异常，如果是则认为此账号已经存在了
                        LOGGER.error("{}", error)
                        throw error
                    }.flatMap {
                        Mono.just(MemberDto(CORE_ERROR_CODE.SUCESS, it.uid, account))
                    }
            }
    }

    /**
     * 电话号码的密码 取最后几位
     */
    private fun getTelephonePwd(telephone: String): String {
        return telephone.takeLast(TELEPHONE_ACCOUNT_PWD_LAST)
    }

}