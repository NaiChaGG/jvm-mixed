package io.github.jojoti.sserviceaccount.entity

import javax.persistence.*

@Entity
@Table(name = "member")
data class MemberEntity(
    @Column(nullable = false, columnDefinition = "char(50)")
    var pwd: String,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "BIGINT(20) UNSIGNED")
    val id: Long = 0L,
    @Column(columnDefinition = "BIGINT(20) UNSIGNED DEFAULT 0")
    val createdAt: Long = System.currentTimeMillis()
)