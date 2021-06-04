# jvm 技术整合
## 1. maven starter 
+ 类似 `spring boot starter` 一些场景库的启动配置 是 spring boot 的超集
+ 技术中立，对 `grpc`、`akka` 等一些 `spring` 家族之外的解决方案进行集成
+ 应用场景更灵活，不局限于 web, 不局限于`响应式编程`来解决线程复用问题

## 2. grpc starter
+ grpc server starter 支持监听多个端口，应对服务之间调用走不同端口访问的模式
+ grpc server 支持向 `etcd`/`consul`/`zk` 等注册发现服务里注册 
+ grpc server 支持多种模式，比如 `spring boot`(servlet) , `akka`, `kotlin coroutinue` , `rxjava3` 等等 server 模式下运行
+ grpc client starter 支持 服务发现
+ [更多文档](./grpc-starter)

## 3. util 该目录是一些从业以来的遇到的小需求，抽象成库
+ version-code 1.0.1 -> 10001 版本转化为数据，在数据库可以使用 > 查询出来, 而非 `1.1.1` 字符串需要依赖db的函数进行赛选查询
+ [更多模块](./util)

## 4. single-service 一些通用业务的基本抽象
+ account 简易的账户系统
+ [更多单应用](./single-service)

## 5. examples 一些学习笔记
+ [`dagger` 学习笔记](./examples/dagger-project-server-with-server/src/main/java/io/github/jojoti/examples/dagger)
+ [更多笔记](./examples)