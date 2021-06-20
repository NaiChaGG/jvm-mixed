# grpc starters

提供了各种模式下 `grpc java` 的 `server`&`client` 启动方式

## 基础代码模块

+ grpc-common-discovery 注册发现通用 模块 server & client 都需要接入 注册发现
+ grpc-common-multi-server 启动 多个 grpc server 通用代码
+ grpc-common-multi-client 启动 多个 grpc client 通用代码

## 附加功能集成 session 访问控制

+ grpc-common-server-session session 支持
+ grpc-common-server-ram grpc server 访问控制 basic api , 包括对 session 和 access controller，这里需要调研一下 fixme spring security

## 支持模式

### 1: spring 家族

+ grpc-starter-spring-boot2 spring boot server starter
+ grpc-starter-spring-boot2-kotlin spring boot server starter kotlin 支持
+ grpc-starter-spring-boot2-session-redis redis session 支持

### N.更多模式

+ 需要集成更多 server 模式，请发起`issue`,如 `quasar`, `netty event loop` 等等
