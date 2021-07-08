/*
 * Copyright 2021 JoJo Wang , homepage: https://github.com/jojoti/experiment-jvm.
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

import java.lang.annotation.*;

/**
 * 3rd 设计 参考 阿里巴巴 https://github.com/alibaba/p3c
 *
 * @author JoJo Wang
 * @link github.com/jojoti
 * @see GRpcScope
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@GRpcScopeServiceInterceptor(scope = @GRpcScope(value = GRpc3RDService.scopeName))
public @interface GRpc3RDInterceptor {
}
