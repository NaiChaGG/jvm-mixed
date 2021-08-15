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

package io.github.jojoti.grpcstartersbexamples;

import io.github.jojoti.grpcstartersb.GRpcScope;
import io.github.jojoti.grpcstartersb.GRpcScopeServiceInterceptor;

import java.lang.annotation.*;

/**
 * @author JoJo Wang
 * @link github.com/jojoti
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@GRpcScopeServiceInterceptor(scope = @GRpcScope(value = GRpcMixed1Service.scopeName))
public @interface GRpcMixed1Interceptor {
}
