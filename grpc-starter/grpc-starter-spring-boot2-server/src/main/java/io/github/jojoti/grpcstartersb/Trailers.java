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

import io.github.jojoti.utilguavaext.ErrorKey;
import io.grpc.Metadata;
import io.grpc.Status;
import io.grpc.StatusException;

/**
 * https://juejin.cn/post/6943618407393099807
 * <p>
 * grpc Trailers Vs Header
 * <p>
 * Trailers 表示请求结束时发送的 头信息，Header 是建立连接时发送的头信息
 * <p>
 *
 * @author JoJo Wang
 * @link github.com/jojoti
 */
public interface Trailers {

    // onError 调用此方法 不必抛出异常
    // kotlin 下直接 throw 参考 grpc + kotlin 生成的 代码实现如何返回错误
    // 使用 此方法可以传递更多的 头信息给客户端
    Metadata.Key<String> X_ERROR_METADATA_KEY = Metadata.Key.of("x-err", Metadata.ASCII_STRING_MARSHALLER);

    static StatusException newErrorCode(int error) {
        final var trailers = new Metadata();
        trailers.put(X_ERROR_METADATA_KEY, String.valueOf(error));
        return new StatusException(Status.INTERNAL, trailers);
    }

    static StatusException newErrorCode(int error, Metadata mergeTrailers) {
        final var trailers = new Metadata();
        trailers.merge(mergeTrailers);
        trailers.put(X_ERROR_METADATA_KEY, String.valueOf(error));
        return new StatusException(Status.INTERNAL, trailers);
    }

    static <T extends Enum<T>> StatusException newErrorCode(ErrorKey<T> error) {
        return newErrorCode(error.getValue());
    }

    static <T extends Enum<T>> StatusException newErrorCode(ErrorKey<T> error, Metadata mergeTrailers) {
        return newErrorCode(error.getValue(), mergeTrailers);
    }

    static StatusException newErrorTraces(Throwable exception) {
        return Status.fromCode(Status.INTERNAL.getCode())
                .withDescription(exception.getMessage())
                .withCause(exception)
                .asException();
    }

    static StatusException newArgsError() {
        return Status.INVALID_ARGUMENT.asException();
    }

}
