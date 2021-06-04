# grpc java starter
提供了各种模式下 `grpc java` 的启动方式
## 支持的模式
+ grpc-starter-nospring-dagger-akka, 该模式使用 `dagger` 依赖注入，使用 `akka` 来跑异步模型，借助 `akka` 的生态可以高校开发基础服务
+ grpc-starter-nospring-dagger-kt-coroutinues ， 该模式使用 `dagger` 注入，使用 `kotlin` 协程来跑异步线程，可以粘合各种java库，并且和复用线程模型
+ grpc-starter-nospring-dagger-rxjava3 ， 该模式使用 `dagger` 注入，使用 `rxjava3` 的模式来 粘合各种库
+ grpc-starter-spring-boot2 ，该模式完全依赖于 `spring` 一套解决方案, [为什么要造轮子](./grpc-starter-spring-boot2/)
+ 需要集成更多 server 模式，请发起`issue`,如 `quasar`, `netty event loop` 等等