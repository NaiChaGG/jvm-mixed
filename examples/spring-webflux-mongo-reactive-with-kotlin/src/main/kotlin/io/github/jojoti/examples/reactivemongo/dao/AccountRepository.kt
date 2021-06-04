package io.github.jojoti.examples.reactivemongo.dao

import io.github.jojoti.examples.reactivemongo.entity.AccountEntity
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono

@Repository
interface AccountRepository : ReactiveMongoRepository<AccountEntity, String> {

    fun findByTelephoneNoPass(telephone: String): Mono<AccountEntity> {
        return findByAccountAndTypeInAndLoginEnabled(telephone, listOf(AccountEntity.AccountType.TELEPHONE_NO_PASS))
    }

    fun findByTelephone(telephone: String): Mono<AccountEntity> {
        return findByAccountAndTypeInAndLoginEnabled(telephone, listOf(AccountEntity.AccountType.TELEPHONE))
    }

    fun findByEmail(telephone: String): Mono<AccountEntity> {
        return findByAccountAndTypeInAndLoginEnabled(telephone, listOf(AccountEntity.AccountType.EMAIL))
    }

    fun findByMixed(telephone: String): Mono<AccountEntity> {
        return findByAccountAndTypeInAndLoginEnabled(telephone, listOf(AccountEntity.AccountType.MIXED))
    }

    fun accountExists(account: String): Mono<AccountEntity> {
        return findByAccountAndTypeInAndLoginEnabled(account, AccountEntity.AccountType.values().asList())
    }

    fun findByAccountAndTypeInAndLoginEnabled(
        account: String,
        type: List<AccountEntity.AccountType>,
        loginEnabled: Boolean = true
    ): Mono<AccountEntity>

}