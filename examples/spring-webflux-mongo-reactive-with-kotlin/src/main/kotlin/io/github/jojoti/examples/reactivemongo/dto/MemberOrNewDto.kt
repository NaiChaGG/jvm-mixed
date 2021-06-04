package io.github.jojoti.examples.reactivemongo.dto

import com.google.protobuf.Internal


data class MemberOrNewDto(
    val errorCode: Internal.EnumLite,
    val uid: Long = 0,
    val account: String = "",
    val isNew: Boolean = false
)