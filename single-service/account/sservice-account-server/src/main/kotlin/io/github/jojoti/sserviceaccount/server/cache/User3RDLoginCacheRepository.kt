package io.github.jojoti.sserviceaccount.cache

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface User3RDLoginCacheRepository : CrudRepository<User3RDLoginEntity, String>