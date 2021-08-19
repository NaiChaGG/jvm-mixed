package io.github.jojoti.grpcstartersbram;

import io.github.jojoti.grpcstartersb.GRpcScope;
import io.github.jojoti.utilguavaext.DTOBool;
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
public class RAMAccessInterceptorConditionSession implements RAMAccessInterceptorCondition {

    /**
     * 强制加在 所有 开启了 ram 的访问控制上
     */
    @Override
    public final boolean matches(GRpcScope gRpcScope) {
        return true;
    }

    @Override
    public int ordered() {
        return -10;
    }

    @Override
    public RAMAccessInterceptor newRAMAccessInterceptor() {
        return new RAMAccessInterceptor() {
            @Override
            public <ReqT, RespT> DTOBool<ServerCall.Listener<ReqT>> checkNext(RegisterRAMItem ram, ServerCall<ReqT, RespT> call, Metadata headers, ServerCallHandler<ReqT, RespT> next) {
                // 判断用户是否登陆
                if (!checkSessionPass(ram)) {
                    // 权限不足
                    final var error = Status.fromCode(Status.UNAUTHENTICATED.getCode()).withDescription("Auth failed, please check session");
                    call.close(error, new Metadata());
                    return DTOBool.failed(new ServerCall.Listener<>() {
                    });
                }
                return DTOBool.ok();
            }
        };
    }

    private boolean checkSessionPass(RAMAccessInterceptor.RegisterRAMItem ram) {
        return ram.isAllowAnonymous() || SessionInterceptor.USER_NTS.get().isLogin();
    }

}
