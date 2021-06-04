package io.github.jojoti.examples.reactivemongo.dao

import io.github.jojoti.examples.reactivemongo.entity.KvEntity
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository

@Repository
interface KvRepository : ReactiveMongoRepository<KvEntity, String>
