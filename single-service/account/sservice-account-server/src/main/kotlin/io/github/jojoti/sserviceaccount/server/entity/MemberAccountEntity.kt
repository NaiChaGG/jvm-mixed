package io.github.jojoti.sserviceaccount.entity

import javax.persistence.*

/**
 * 一个UID存在多个账号
 *
@Entity
@Table(name = "member_account", indexes = [
Index(columnList = "uid"),
Index(columnList = "account,accountType", unique = true)
])
@Deprecated("fix me tbd")
data class MemberAccountEntity(
@Column(length = 120)
val account: String,
@Column(columnDefinition = "TINYINT(4) UNSIGNED COMMENT '用户账户类型, 0 email, 1 手机号, 2 自定义'")
val accountType: AccountType,
@Column(columnDefinition = "BIGINT(20) UNSIGNED")
val uid: Long,

@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
@Column(columnDefinition = "BIGINT(20) UNSIGNED")
val id: Long = 0L,
@Column(columnDefinition = "BIGINT(20) UNSIGNED")
val createdAt: Long = System.currentTimeMillis()
)

enum class AccountType {
EMAIL,
TELEPHONE,
MIXED
}