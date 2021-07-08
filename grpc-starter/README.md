# grpc starters

提供了各种模式下 `grpc java` 的 `server`&`client` 启动方式

## 为什么要再造轮子

已经存在的 grpc java spring boot starter 库如下：

+ [yidongnan/grpc-spring-boot-starter](https://github.com/yidongnan/grpc-spring-boot-starter)
+ [LogNet/grpc-spring-boot-starter](https://github.com/LogNet/grpc-spring-boot-starter)
+ [更多](https://github.com/search?q=grpc+starter)

## 关于 LogNet/grpc-spring-boot-starter

生产的项目使用了此项目作为 grpc starter 因为其有 注册发现的特性，以及 `spring boot autoconfigure`,遇到的问题:

+ [监听多个端口](https://github.com/LogNet/grpc-spring-boot-starter/issues/174)
+ [不引入 spring security接口客户端接口与服务端接口权限问题](https://github.com/LogNet/grpc-spring-boot-starter/issues/213)
  基于以上不同的理念重新开发了 grpc starter 并扩展了对 spring 家族之外的 server 模式的支持

## 关于 grpc-starter-spring-boot2

+ 基于多端口构建 grpc server，终端流量与 service 之间的流量彻底分开，走不同的 `interceptor` 校验权限
+ etcd/consul/zk/eureka/nacos 等注册发现的支持,
  需要更多注册发现支持，请提 [`issue`](https://github.com/jojoti/experiment-jvm/issues/new)或 `pr`

## 基础代码模块

+ grpc-common-discovery 注册发现通用 模块 server & client 都需要接入 注册发现
+ grpc-common-multi-server 启动 多个 grpc server 通用代码
+ grpc-common-multi-client 启动 多个 grpc client 通用代码

## 附加功能集成 session 访问控制

+ grpc-common-server-ram grpc server 访问控制 basic api , 包括对 session 和 access controller，这里需要调研一下 fixme spring security

## 支持模式

### 1: spring 家族

+ grpc-starter-spring-boot2 spring boot server starter
+ grpc-starter-spring-boot2-kotlin spring boot server starter kotlin 支持
+ grpc-starter-spring-boot2-session-redis redis session 支持

### N.更多模式

+ 需要集成更多 server 模式，请发起`issue`,如 `quasar`, `netty event loop` 等等
