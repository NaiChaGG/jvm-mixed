/*
 * Copyright 2021 JoJo Wang , homepage: https://github.com/jojoti/jvm-mixed.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.jojoti.grpcstartersb;

import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 多个 grpc service 作用域
 * <p>
 * 默认 scope 为:
 * primary 主要用于暴露给终端的 api
 * admin 管理员调用的接口
 * 2rd 给当前应用边界之外的公司内部其它项目调用 统称 二方调用 参考 参考 阿里巴巴 https://github.com/alibaba/p3c 工程结构一章 对二方库的定义
 * 3rd 给公司外部的项目使用
 *
 * @author JoJo Wang
 * @link github.com/jojoti
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface GRpcScope {

    String value() default "default";

}
