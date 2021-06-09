package io.github.jojoti.grpcstatersb;

import io.grpc.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 全局异常处理
 *
 * https://stackoverflow.com/questions/39797142/how-to-add-global-exception-interceptor-in-grpc-server
 * https://github.com/grpc/grpc-java/issues/1552
 * https://skyao.io/learning-grpc/server/status/exception_process.html
 *
 * @author JoJo Wang
 * @link github.com/jojoti
 */
@GRpcGlobalInterceptor
public class GlobalExceptionInterceptor implements ServerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionInterceptor.class);

    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(
            ServerCall<ReqT, RespT> call, Metadata requestHeaders, ServerCallHandler<ReqT, RespT> next) {

        ServerCall.Listener<ReqT> delegate = next.startCall(call, requestHeaders);

        return new ForwardingServerCallListener.SimpleForwardingServerCallListener<>(delegate) {
            @Override
            public void onHalfClose() {
                try {
                    super.onHalfClose();
                } catch (Exception e) {
                    log.error("grpc close", e);
                    call.close(Status.INTERNAL.withCause(e), new Metadata());
                }
            }
        };
    }

}