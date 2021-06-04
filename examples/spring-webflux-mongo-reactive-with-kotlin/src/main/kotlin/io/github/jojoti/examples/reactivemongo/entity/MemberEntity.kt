package io.github.jojoti.examples.reactivemongo.entity

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field

@Document(collection = "member")
data class MemberEntity(
    /**
     * 账号密码
     */
    @Field
    val pwd: String,
    /**
     * 用户UID
     */
    @Id
    var uid: Long = 0
)