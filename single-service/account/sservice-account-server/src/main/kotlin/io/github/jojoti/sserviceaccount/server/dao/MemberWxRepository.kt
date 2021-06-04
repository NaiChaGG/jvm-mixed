package io.github.jojoti.sserviceaccount.dao

import io.github.jojoti.sserviceaccount.entity.MemberWxEntity
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface MemberWxRepository : CrudRepository<MemberWxEntity, Long> {

    fun getByOpenIdAndAppId(openId: String, wxAppId: String): MemberWxEntity?

}