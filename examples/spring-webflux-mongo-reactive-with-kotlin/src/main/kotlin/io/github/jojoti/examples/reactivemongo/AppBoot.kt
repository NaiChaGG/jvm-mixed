package io.github.jojoti.examples.reactivemongo

import io.github.jojoti.examples.reactivemongo.entity.MemberEntity
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.runApplication

@SpringBootApplication
// 实体扫描地址
@EntityScan(basePackageClasses = [MemberEntity::class])
class AppBoot

fun main(args: Array<String>) {
    runApplication<AppBoot>(*args)
}