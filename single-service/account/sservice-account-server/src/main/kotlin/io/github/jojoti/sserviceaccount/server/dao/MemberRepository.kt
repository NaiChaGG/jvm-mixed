package io.github.jojoti.sserviceaccount.dao

import io.github.jojoti.sserviceaccount.entity.MemberEntity
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface MemberRepository : CrudRepository<MemberEntity, Long>