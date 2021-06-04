package io.github.jojoti.sserviceaccount

import org.springframework.cache.annotation.EnableCaching
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import java.lang.annotation.Documented
import java.lang.annotation.Inherited

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@Documented
@Inherited
@EnableJpaRepositories
@EnableCaching
annotation class EnableUCServer