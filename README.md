# jvm 技术整合

## starters

### 1.0 basic starter
+ 类似 `spring boot starter` 一些场景库的启动配置 是 spring boot 的超集
+ 技术中立，对 `grpc`、`akka` 等一些 `spring` 家族之外的解决方案进行集成
+ 应用场景更灵活，不局限于 web, 不局限于`响应式编程`来解决线程复用问题

### 1.1. grpc starter

+ grpc server starter 支持监听多个端口，应对服务之间调用走不同端口访问的模式
+ grpc server 支持向 `etcd`/`consul`/`zk` 等注册发现服务里注册
+ grpc server 支持多种模式，比如 `spring boot`(servlet) , `akka`, `kotlin coroutinues` , `rxjava3` 等等 server 模式下运行
+ grpc client starter 支持 服务发现
+ [更多文档](./grpc-starter)

### 1.n 关于 starter-parent, starter-parent-dagger, starter-parent-kotlin 派生出来的多继承问题
+ 举例子 starter-parent-kotlin, starter-parent 两个 starter 是互相不兼容的，类似c++多继承派生出了一个菱形
+ 假设引入 starters 是一个多模块项目，存在 java与kotlin两种语言写的项目， 该多模块根目录则不应该继承 starter
+ 继承导致无法为单独语言导入特有的 starter
+ 解决方案，则是根目录不继承，而是引入一个 parent 作为中专，各自要继承的 starter 各种继承，然后各个模块的交互则走 parent pom 进行依赖
```
总结: 除非已经确定是单模块项目，否则不要再根目录下使用继承，会形成错误的使用习惯!!!
```
+ [解决方案可以参考](./grpc-starter)

## 3. util 该目录是一些从业以来的遇到的小需求，抽象成库

+ version-code 1.0.1 -> 10001 版本转化为数据，在数据库可以使用 > 查询出来, 而非 `1.1.1` 字符串需要依赖db的函数进行赛选查询
+ [更多模块](./util)

## 4. single-service 一些通用业务的基本抽象

+ account 简易的账户系统
+ [更多单应用](./single-service)

## 5. examples 一些学习笔记

+ [`dagger` 学习笔记](./examples/dagger-project-server-with-server/src/main/java/io/github/jojoti/examples/dagger)
+ [更多笔记](./examples)
