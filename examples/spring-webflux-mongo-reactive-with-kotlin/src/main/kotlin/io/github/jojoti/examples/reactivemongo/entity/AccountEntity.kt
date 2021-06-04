package io.github.jojoti.examples.reactivemongo.entity

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.CompoundIndex
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field

@Document(collection = "account")
// 创建一个复合索引, 都是用正序逻辑
@CompoundIndex(name = "uid_account_idx", def = "{'uid': 1, 'type': 1}", unique = true)
data class AccountEntity(
    /**
     * 账号
     */
    @Indexed(unique = true)
    @Field val account: String,
    /**
     * 用户uid
     */
    @Field
    val uid: Long,
    /**
     * 账号类型
     */
    @Field
    val type: AccountType? = AccountEntity.AccountType.MIXED,
    /**
     * 是否启用登录
     */
    @Field
    val loginEnabled: Boolean = false,
    /**
     * 文档唯一ID
     */
    @Id var objectId: String = ""
) {

    enum class AccountType {
        MIXED, EMAIL, TELEPHONE, TELEPHONE_NO_PASS
    }

}