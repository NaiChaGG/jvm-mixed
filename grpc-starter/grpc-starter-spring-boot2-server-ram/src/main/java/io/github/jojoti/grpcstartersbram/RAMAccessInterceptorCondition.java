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

package io.github.jojoti.grpcstartersbram;

import io.github.jojoti.grpcstartersb.GRpcScope;

/**
 * https://stackoverflow.com/questions/68164315/access-message-request-in-first-grpc-interceptor-before-headers-in-second-grpc-i
 * <p>
 * 此接口是对拦截器行为的扩展
 *
 * @author JoJo Wang
 * @link github.com/jojoti
 * @see io.grpc.ServerInterceptor
 */
public interface RAMAccessInterceptorCondition {

    /**
     * 作用域匹配上了
     */
    boolean matches(GRpcScope gRpcScope);

    /**
     * 排序值小的在前面执行 按照 约定进行排序
     */
    default int ordered() {
        return 0;
    }

    /**
     * 获取 scope 拦截器的 实例
     */
    RAMAccessInterceptor newRAMAccessInterceptor();

}
