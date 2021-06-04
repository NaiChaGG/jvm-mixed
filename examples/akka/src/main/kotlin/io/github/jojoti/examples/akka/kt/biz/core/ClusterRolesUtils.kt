package io.github.jojoti.examples.akka.kt.biz.core

import akka.cluster.Member
import io.github.jojoti.moo.akka.cluster.proto.ActorClusterRoles

object ClusterRolesUtils {

    fun fronetendExtraRoles(selfRoles: scala.collection.Set<String>, member: Member)
            : ActorClusterRolesEntity {

        // 获取当前 app name
        val myRoleName = mooRoleName(selfRoles)

        // 传入角色必须是frontend
        if (!selfRoles.contains(ActorClusterRoles.MOO_APP_FRONTEND.name)) {
            throw IllegalArgumentException("Used role $myRoleName must be frontend.")
        }

        // 获取传入角色appName
        val memberMooAppName = mooRoleName(member.roles())

        // 传入角色必须是 backend
        if (!member.hasRole(ActorClusterRoles.MOO_APP_BACKEND.name)) {
            throw IllegalArgumentException("Used role $memberMooAppName must be backend.")
        }

        // 该节点是否允许我监听
        val allowWatching = member.hasRole(ActorClusterRoles.MOO_ALLOW_WATCHING.name + "_" + myRoleName)
        // 该节点是否关心我
        val loveWatching = member.hasRole(ActorClusterRoles.MOO_LOVE_WATCHING.name + "_" + myRoleName)

        return ActorClusterRolesEntity(myRoleName, memberMooAppName, allowWatching, loveWatching)
    }

    fun backendExtraRoles(selfRoles: scala.collection.Set<String>, member: Member): ActorClusterRolesEntity {

        // 获取当前 app name
        val myRoleName = mooRoleName(selfRoles)

        // 传入角色必须是 backend
        if (!selfRoles.contains(ActorClusterRoles.MOO_APP_BACKEND.name)) {
            throw IllegalArgumentException("Used role $myRoleName must be backend.")
        }

        // 获取传入角色 appName
        val memberMooAppName = mooRoleName(member.roles())

        // 传入角色必须是 frontend
        if (!member.hasRole(ActorClusterRoles.MOO_APP_FRONTEND.name)) {
            throw IllegalArgumentException("Used role $memberMooAppName must be frontend.")
        }

        val allowWatching = selfRoles.contains(ActorClusterRoles.MOO_ALLOW_WATCHING.name + "_" + memberMooAppName)
        val loveWatching = selfRoles.contains(ActorClusterRoles.MOO_LOVE_WATCHING.name + "_" + memberMooAppName)

        return ActorClusterRolesEntity(myRoleName, memberMooAppName, allowWatching, loveWatching)
    }

    private fun mooRoleName(selfRoles: scala.collection.Set<String>): String {

        //
        var memberMooAppName = ""

        for (role in selfRoles) {
            val newRole = role.trimMargin(ActorClusterRoles.MOO_NAME_PREFIX.name + "_")
            if (newRole != role) {
                //
                memberMooAppName = newRole
                break
            }
        }

        if (memberMooAppName.isEmpty()) {
            throw IllegalArgumentException("The node is not found backend name defined.")
        }

        return memberMooAppName
    }


}

data class ActorClusterRolesEntity(
    val myName: String,
    val nodeName: String,
//        val isSameRole : Boolean,
    val allowWatching: Boolean,
    val loveWatching: Boolean
)