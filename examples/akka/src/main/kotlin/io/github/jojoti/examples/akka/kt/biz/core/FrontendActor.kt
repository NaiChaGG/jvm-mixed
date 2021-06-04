package io.github.jojoti.examples.akka.kt.biz.core

import akka.actor.AbstractActor
import akka.actor.ActorRef
import akka.cluster.Cluster
import akka.cluster.ClusterEvent
import akka.event.Logging
import com.google.common.collect.Maps
import io.github.jojoti.moo.akka.cluster.proto.ActorGetRoutesReply
import io.github.jojoti.testing.akka.kt.biz.coreV1.proto.UserActorRequest

/**
 * @author JoJo Wang
 * Date: 2019/1/7
 * Time: 11:15
 */
class FrontendActor : AbstractActor(), FrontendRouter {

    private val log = Logging.getLogger(context.system, this)
    private val cluster = Cluster.get(context.system)

    private val routeUris = Maps.newConcurrentMap<String, ActorRef>()

    private val routers = Maps.newConcurrentMap<String, ActorRef>()

    //subscribe to io.github.jojoti.testing.akka.cluster changes
    override fun preStart() {
        cluster.subscribe(
            self, ClusterEvent.initialStateAsEvents(),
            ClusterEvent.MemberEvent::class.java, ClusterEvent.UnreachableMember::class.java
        )
    }

    //re-subscribe when restart
    override fun postStop() {
        // 取消订阅
        cluster.unsubscribe(self)
    }

    override fun createReceive(): AbstractActor.Receive {

        return receiveBuilder()
            .match(ClusterEvent.MemberUp::class.java)
            { mUp ->
                // 如果角色和自己一样
//                    if (mUp.member().hasRole(ActorClusterRoles.MOO_FRONTEND.name)) {
//
//                        // 去其它节点会发过来 同步路由的消息
//                    } else {
//
//                    }

                //
                for (role in mUp.member().roles) {
                    when {
//                            // 以 backend 开头
//                            role.indexOf(ActorClusterRoles.MOO_BACKEND.name) == 0 -> {
//                                // 且对方允许我监听它
//                                if (role.indexOf(ActorClusterRoles.MOO_ALLOW_WATCHING.name) == 0) {
//                                    // 发起同步路由请求
//                                    this.sender.tell(ActorGetRoutes.getDefaultInstance(), this.self)
//                                }
//                            }
//                            else -> {
//                            }
                    }
                }

                log.info("Member is Up: {}", mUp.member())
            }

            .match(ClusterEvent.UnreachableMember::class.java)
            { mUnreachable -> log.info("Member detected as unreachable: {}", mUnreachable.member()) }
            .match(ClusterEvent.MemberRemoved::class.java)
            { mRemoved -> log.info("Member is Removed: {}", mRemoved.member()) }
            .match(ClusterEvent.MemberEvent::class.java)
            {
                // ignore
            }
            .match(ActorGetRoutesReply::class.java, this::addRoute)
            .match(UserActorRequest::class.java, this::dispatching)
            .build()
    }

    private fun addRoute(routes: ActorGetRoutesReply) {
        val path = cluster.remotePathOf(sender)





        path.toString() + "/r"

        path.child("/gateway/route/r0")
        path.child("/gateway/route/r1")
        path.child("/gateway/route/r2")
        path.child("/gateway/route/r3")

        // /gateway/login

        val newUris = mutableListOf<String>()
//        routes.entitiesList.forEach {
//            when (it.type) {
//                RouterType.RANDOM_GROUP -> {
//                    val route = context.actorOf(RandomGroup(listOf(*it.uriSeedsList.toTypedArray())).props())
//                    // CAS
//                    val oldRoute = routeUris.putIfAbsent(it.uri, route)
//                    if (oldRoute != route) {
//                        // 转移旧的 actor router
//                        context.stop(oldRoute)
//                    }
//                }
//                else -> {
//                    throw IllegalArgumentException("Unknown type ${it.type}")
//                }
//            }
//            newUris.add(it.uri)
//        }

        routeUris.forEach { k, u ->
            if (!newUris.contains(k)) {
                // 移除引用
                routeUris.remove(k)
                // 迁移旧的 actor
                context.stop(u)
            }
        }
    }

    override fun dispatching(request: UserActorRequest) {
        val router = routeUris[request.uri]
        // 回复 路由没有找到
            ?: return this.sender.tell(PbExchangeMessage.errUriNotFound(request), self)
        router.forward(request, context)
    }

}