package io.github.jojoti.sserviceaccount.cache

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface OAuth2CacheRepository : CrudRepository<OAuth2CacheEntity, String>