# 为什么要再造轮子
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
+ etcd/consul/zk/eureka/nacos 等注册发现的支持, 需要更多注册发现支持，请提 [`issue`](https://github.com/jojoti/experiment-jvm/issues/new)或 `pr`