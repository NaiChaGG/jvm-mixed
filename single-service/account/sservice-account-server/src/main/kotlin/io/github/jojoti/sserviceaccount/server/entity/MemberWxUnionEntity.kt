package io.github.jojoti.sserviceaccount.entity

import javax.persistence.*

@Entity
@Table(
    name = "member_wx_union_id", indexes = [
        Index(columnList = "uid")
    ]
)
data class MemberWxUnionEntity(
    @Column(nullable = false, columnDefinition = "BIGINT(20) UNSIGNED")
    val uid: Long,
    @Column(unique = true, length = 120)
    val unionId: String,
    @Column(length = 120)
    val firstBindAppId: String,
    @Column(length = 120)
    val firstBindOpenId: String,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "BIGINT(20) UNSIGNED")
    val id: Long = 0L,
    @Column(columnDefinition = "BIGINT(20) UNSIGNED DEFAULT 0")
    val createdAt: Long = System.currentTimeMillis()
)