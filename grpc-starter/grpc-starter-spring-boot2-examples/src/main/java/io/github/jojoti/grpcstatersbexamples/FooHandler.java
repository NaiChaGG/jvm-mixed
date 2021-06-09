package io.github.jojoti.grpcstatersbexamples;

import io.github.jojoti.grpcstartersb.FooGrpc;
import io.github.jojoti.grpcstartersb.Hello;
import io.github.jojoti.grpcstatersb.GRpcPrivateService;
import io.grpc.stub.StreamObserver;

/**
 *
 * @author JoJo Wang
 * @link github.com/jojoti
 */
@GRpcPrivateService
public class FooHandler extends FooGrpc.FooImplBase {

    public FooHandler() {

    }

    @Override
    public void bar(Hello.BarRequest request, StreamObserver<Hello.BarResponse> responseObserver) {
        super.bar(request, responseObserver);
    }

}
