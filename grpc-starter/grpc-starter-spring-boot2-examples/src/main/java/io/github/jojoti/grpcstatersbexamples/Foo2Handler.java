package io.github.jojoti.grpcstatersbexamples;

import io.github.jojoti.grpcstartersb.Foo2Grpc;
import io.github.jojoti.grpcstartersb.Hello2;
import io.github.jojoti.grpcstatersb.GRpcPrimaryService;
import io.github.jojoti.grpcstatersb.GRpcServiceInterceptors;
import io.grpc.stub.StreamObserver;

/**
 *
 * @author JoJo Wang
 * @link github.com/jojoti
 */
@GRpcPrimaryService
@GRpcServiceInterceptors(interceptors = App1Interceptor.class)
public class Foo2Handler extends Foo2Grpc.Foo2ImplBase {

    public Foo2Handler() {

    }

    @Override
    public void bar(Hello2.BarRequest2 request, StreamObserver<Hello2.BarResponse2> responseObserver) {
        super.bar(request, responseObserver);
    }

}
