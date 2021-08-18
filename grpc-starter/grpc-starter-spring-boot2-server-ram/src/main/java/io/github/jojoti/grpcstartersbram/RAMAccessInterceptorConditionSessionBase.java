package io.github.jojoti.grpcstartersbram;

import io.grpc.Metadata;
import io.grpc.ServerCall;
import io.grpc.ServerCallHandler;
import io.grpc.Status;

/**
 * 默认为 所有 session 打开 会话控制
 *
 * @author JoJo Wang
 * @link github.com/jojoti
 */
public abstract class RAMAccessInterceptorConditionSessionBase implements RAMAccessInterceptorCondition {

    @Override
    public RAMAccessInterceptor newRAMAccessInterceptor() {
        return new RAMAccessInterceptor() {
            @Override
            public <ReqT, RespT> ServerCall.Listener<ReqT> checkNext(RegisterRAMItem ram, ServerCall<ReqT, RespT> call, Metadata headers, ServerCallHandler<ReqT, RespT> next) {
                // 判断用户是否登陆
                if (!checkSessionPass(ram)) {
                    // 权限不足
                    final var error = Status.fromCode(Status.UNAUTHENTICATED.getCode()).withDescription("Auth failed, please check session");
                    call.close(error, new Metadata());
                    return new ServerCall.Listener<>() {
                    };
                }
                return next.startCall(call, headers);
            }
        };
    }

    protected boolean checkSessionPass(RAMAccessInterceptor.RegisterRAMItem ram) {
        return ram.isAllowAnonymous() || SessionInterceptor.USER_NTS.get().isLogin();
    }

}
