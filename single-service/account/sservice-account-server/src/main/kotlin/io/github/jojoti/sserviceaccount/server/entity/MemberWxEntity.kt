package io.github.jojoti.sserviceaccount.entity

import javax.persistence.*

/**
 * 一个用户可能存在多个wx open id
 */
@Entity
@Table(
    name = "member_wx", indexes = [
        Index(columnList = "uid"),
        Index(columnList = "openId,appId", unique = true)
    ]
)
data class MemberWxEntity(
    @Column(nullable = false, columnDefinition = "BIGINT(20) UNSIGNED")
    val uid: Long,
    @Column(nullable = false, length = 120)
    val appId: String = "",
    @Column(nullable = false, length = 120)
    val openId: String = "",
    @Column(nullable = false, length = 120)
    var unionId: String = "",
    @Column(nullable = false)
    var unionIdIsValid: Boolean = false,
    // 有可能 open id 已经生成了两个账号，此时 再开通 union id
    // 这个union id 对于旧账号就无效

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "BIGINT(20) UNSIGNED")
    val id: Long = 0L,
    @Column(columnDefinition = "BIGINT(20) UNSIGNED DEFAULT 0")
    val createdAt: Long = System.currentTimeMillis()
) {

    fun unionIdIsBind(): Boolean {
        return unionId.isNotEmpty()
    }

}
/**
 *
 * wxRegisterOrCreate app id 1 -> open id 1
 * wxRegisterOrCreate app id 2 -> open id 2
 *
 *
 */


