package io.github.jojoti.grpcstatersbexamples;

import io.github.jojoti.grpcstartersb.Foo3Grpc;
import io.github.jojoti.grpcstartersb.Hello3;
import io.grpc.stub.StreamObserver;

/**
 *
 * @author JoJo Wang
 * @link github.com/jojoti
 */
@GRpcMixed1Service
public class Foo3Handler extends Foo3Grpc.Foo3ImplBase {

    public Foo3Handler() {

    }

    @Override
    public void bar(Hello3.BarRequest3 request, StreamObserver<Hello3.BarResponse3> responseObserver) {
        super.bar(request, responseObserver);
    }

}
