package io.github.jojoti.grpcstatersbexamples;

import com.google.common.base.Strings;
import io.grpc.*;

/**
 * @author JoJo Wang
 * @link github.com/jojoti
 */
@GRpcMixed1Interceptor
public class App1Interceptor implements ServerInterceptor {

    public static final Context.Key<Long> USER_ID_NTS = Context.key("userId");
    private static final Metadata.Key<String> USER_ID_METADATA_KEY = Metadata.Key.of("x-user-id", Metadata.ASCII_STRING_MARSHALLER);

    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(ServerCall<ReqT, RespT> serverCall, Metadata metadata, ServerCallHandler<ReqT, RespT> serverCallHandler) {
        var get = metadata.get(USER_ID_METADATA_KEY);
        if (Strings.isNullOrEmpty(get)) {
            serverCall.close(Status.UNAUTHENTICATED, new Metadata());
            return new ServerCall.Listener<>() {
            };
        }

        var userId = Long.parseLong(get);

        final var context = Context.current().withValue(USER_ID_NTS, userId);

        return Contexts.interceptCall(context, serverCall, metadata, serverCallHandler);
    }

}