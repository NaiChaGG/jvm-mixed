package io.github.jojoti.grpcstartersbram;

import io.github.jojoti.grpcstartersb.GRpcScope;
import io.grpc.ServiceDescriptor;

/**
 * @author JoJo Wang
 * @link github.com/jojoti
 */
class RAMAccessDeny implements RAMAccess {

    @Override
    public boolean access(Object uuid, RAM.RAMItem ramItem, GRpcScope gRpcScope, ServiceDescriptor serviceDescriptor) {
        return false;
    }

}
