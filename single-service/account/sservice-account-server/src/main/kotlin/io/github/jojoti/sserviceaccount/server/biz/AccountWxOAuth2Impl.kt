package io.github.jojoti.sserviceaccount.biz

import io.github.jojoti.sserviceaccount.dao.MemberRepository
import io.github.jojoti.sserviceaccount.dao.MemberWxRepository
import io.github.jojoti.sserviceaccount.dao.MemberWxUnionRepository
import io.github.jojoti.sserviceaccount.entity.MemberEntity
import io.github.jojoti.sserviceaccount.entity.MemberWxEntity
import io.github.jojoti.sserviceaccount.entity.MemberWxUnionEntity
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class AccountWxOAuth2Impl : AccountWxOAuth2 {

    @Autowired
    private lateinit var memberWx: MemberWxRepository

    @Autowired
    private lateinit var member: MemberRepository

    @Autowired
    private lateinit var memberWxUnion: MemberWxUnionRepository

    override fun wxRegisterOrCreate(wxAppId: String, openId: String, unionId: String): AccountEntity {
        val memberWxEntity = memberWx.getByOpenIdAndAppId(openId, wxAppId)
        return when (memberWxEntity) {
            null -> {
                // 查询 该 union id 是否绑定过
                val memberWxUnionEntity = memberWxUnion.getMemberByUnionId(unionId)

                when (memberWxUnionEntity) {
                    null -> {
                        // 创建全新账号
                        val memberEntity = member.save(MemberEntity(""))

                        memberWx.save(MemberWxEntity(memberEntity.id, wxAppId, openId, unionId, true))

                        memberWxUnion.save(MemberWxUnionEntity(memberEntity.id, unionId, wxAppId, openId))

                        AccountEntity(memberEntity.id, true)
                    }
                    else -> {
                        // 根据 api id 创建 wx api user ，uid 根据 union id 查询
                        memberWx.save(MemberWxEntity(memberWxUnionEntity.uid, wxAppId, openId, unionId, true))
                        AccountEntity(memberWxUnionEntity.uid)
                    }
                }
            }
            else -> {
                // union id is bind
                if (memberWxEntity.unionIdIsBind()) {
                    return AccountEntity(memberWxEntity.uid)
                }
                // 查询 该 union id 是否绑定过
                val memberWxUnionEntity = memberWxUnion.getMemberByUnionId(unionId)
                when (memberWxUnionEntity) {
                    null -> {
                        memberWxUnion.save(MemberWxUnionEntity(memberWxEntity.uid, unionId, wxAppId, openId))
                        // 更新 微信 api 绑定信息
                        memberWxEntity.unionId = unionId
                        memberWxEntity.unionIdIsValid = true
                        memberWx.save(memberWxEntity)
                    }
                    else -> {
                        // 更新 微信 api 绑定信息
                        memberWxEntity.unionId = unionId
                        memberWxEntity.unionIdIsValid = memberWxUnionEntity.uid == memberWxEntity.uid
                        memberWx.save(memberWxEntity)
                    }
                }
                AccountEntity(memberWxEntity.uid)
            }
        }

    }

    override fun wxRegisterOrCreate(wxAppId: String, openId: String): AccountEntity {
        val memberWxEntity = memberWx.getByOpenIdAndAppId(openId, wxAppId)
        return when (memberWxEntity) {
            null -> {
                val memberEntity = member.save(MemberEntity(""))

                memberWx.save(MemberWxEntity(memberEntity.id, wxAppId, openId))

                AccountEntity(memberEntity.id, true)
            }
            else -> {
                AccountEntity(memberWxEntity.uid)
            }
        }

    }


}