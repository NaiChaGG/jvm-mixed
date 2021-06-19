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

import com.google.common.base.Preconditions;
import io.grpc.Metadata;
import io.grpc.Status;
import io.grpc.StatusException;

/**
 * https://juejin.cn/post/6943618407393099807
 * grpc Trailers Vs Header
 * Trailers 表示请求结束时发送的 头信息，Hader 是建立连接时发送的头信息
 *
 * @author JoJo Wang
 * @link github.com/jojoti
 */
public interface ErrorCodeTrailers {

    // onError 调用此方法 不必抛出异常
    // kotlin 下直接 throw 参考 grpc + kotlin 生成的 代码实现如何返回错误
    // 使用 此方法可以传递更多的 头信息给客户端
    static StatusException newStatus(int error) {
        Preconditions.checkArgument(error > 0);
        var trailers = new Metadata();
        trailers.put(GlobalExceptionInterceptor.X_ERROR_METADATA_KEY, String.valueOf(error));
        return new StatusException(Status.INTERNAL, trailers);
    }

    static StatusException newStatus(int error, Metadata mergerTrailers) {
        Preconditions.checkArgument(error > 0);
        var trailers = new Metadata();
        trailers.merge(mergerTrailers);
        trailers.put(GlobalExceptionInterceptor.X_ERROR_METADATA_KEY, String.valueOf(error));
        return new StatusException(Status.INTERNAL, trailers);
    }

}
