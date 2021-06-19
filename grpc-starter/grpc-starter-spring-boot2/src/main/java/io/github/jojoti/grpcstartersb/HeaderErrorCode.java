package io.github.jojoti.grpcstartersb;

import io.grpc.Metadata;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;

/**
 * @author JoJo Wang
 * @link github.com/jojoti
 */
public interface HeaderErrorCode {

    Metadata.Key<String> X_ERROR_METADATA_KEY = Metadata.Key.of("x-err", Metadata.ASCII_STRING_MARSHALLER);

    static StatusRuntimeException newCode(int error) {
        final var header = new Metadata();
        header.put(X_ERROR_METADATA_KEY, String.valueOf(error));
        return new StatusRuntimeException(Status.OK, header);
    }

}
