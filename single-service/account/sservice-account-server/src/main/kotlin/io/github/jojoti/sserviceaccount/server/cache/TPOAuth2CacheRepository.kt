package io.github.jojoti.sserviceaccount.cache

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface TPOAuth2CacheRepository : CrudRepository<TPOAuth2CacheEntity, String>