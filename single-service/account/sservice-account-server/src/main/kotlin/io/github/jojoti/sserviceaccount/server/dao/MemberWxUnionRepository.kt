package io.github.jojoti.sserviceaccount.dao

import io.github.jojoti.sserviceaccount.entity.MemberWxUnionEntity
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface MemberWxUnionRepository : CrudRepository<MemberWxUnionEntity, Long> {

    fun getMemberByUnionId(unionId: String): MemberWxUnionEntity?

}