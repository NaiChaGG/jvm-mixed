package io.github.jojoti.examples.reactivemongo.dao

import io.github.jojoti.examples.reactivemongo.entity.MemberEntity
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository

@Repository
interface MemberRepository : ReactiveMongoRepository<MemberEntity, Long>