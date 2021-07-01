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

package io.github.jojoti.grpcstartersbram;

import io.github.jojoti.grpcstartersb.GRpcScope;
import io.grpc.MethodDescriptor;

/**
 * @author JoJo Wang
 * @link github.com/jojoti
 */
class RAMAccessDeny implements RAMAccess {

    @Override
    public boolean access(GRpcScope gRpcScope, RAM ramItem, MethodDescriptor<?, ?> methodDescriptor) {
        return false;
    }

}
