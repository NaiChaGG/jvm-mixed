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

import java.util.List;

/*
 * 此拦截器用于动态拦截某些 scope 因为某些场景不能使用 注解标注，比如，全局拦截器只添加到某个特定 scope 下
 * 如: @GRpcGlobalInterceptor 要添加 到动态特定的 scope 下，那么目前的 注解是无法满足需求的
 *
 * @author JoJo Wang
 * @link github.com/jojoti
 */
public interface DynamicScopeFilter {

    List<String> getScopes();

}
