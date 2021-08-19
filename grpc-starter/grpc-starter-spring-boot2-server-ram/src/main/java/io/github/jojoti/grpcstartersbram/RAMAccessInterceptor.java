package io.github.jojoti.grpcstartersbram;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import io.github.jojoti.utilguavaext.DTOBool;
import io.grpc.*;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * @author JoJo Wang
 * @link github.com/jojoti
 * @see io.grpc.ServerInterceptor
 */
public interface RAMAccessInterceptor {

    // 启动注册 ram 列表权限
    // 这个可能会执行多次
    // 该接口会在 bean 注入之后启动 如果 此时要去调用一些远程的数据库,
    // 需要自行去 CommandLine 里面 延迟运行
    default void onStartRegister(ImmutableList<RegisterRAM> allServices) {

    }

    /**
     * 返回 false 则认为是 条件失败则直接调用 listner
     *
     * @return null 表示走默认失败流程 not null 表示成功
     */
    <ReqT, RespT> DTOBool<ServerCall.Listener<ReqT>> checkNext(RegisterRAMItem ram,
                                                               ServerCall<ReqT, RespT> call,
                                                               Metadata headers,
                                                               ServerCallHandler<ReqT, RespT> next);

    final class RegisterRAM {

        private final BindableService serviceObject;
        private final ImmutableList<RegisterRAMItem> methods;

        RegisterRAM(BindableService serviceObject, ImmutableList<RegisterRAMItem> methods) {
            this.serviceObject = serviceObject;
            this.methods = methods;
        }

        public Object getServiceObject() {
            return serviceObject;
        }

        public ImmutableList<RegisterRAMItem> getMethods() {
            return methods;
        }

    }

    final class RegisterRAMItem {
        private final MethodDescriptor<?, ?> methodDesc;
        private final Method method;

        RegisterRAMItem(MethodDescriptor<?, ?> methodDesc, Method method) {
            this.methodDesc = methodDesc;
            this.method = method;
        }

        /**
         * 找不到报错
         *
         * @param extAnnotationClass
         * @param <T>
         * @return
         */
        public <T extends Annotation> T getRAMExtension(Class<T> extAnnotationClass) {
            return containsRAMExtension(extAnnotationClass, true);
        }

        /**
         * 找不到 返回 null
         *
         * @param extAnnotationClass
         * @param <T>
         * @return
         */
        public <T extends Annotation> T findRAMExtension(Class<T> extAnnotationClass) {
            return containsRAMExtension(extAnnotationClass, false);
        }

        private <T extends Annotation> T containsRAMExtension(Class<T> extAnnotationClass, boolean forced) {
            final var found = AnnotationUtils.findAnnotation(method, extAnnotationClass);
            if (found == null) {
                if (forced) {
                    throw new IllegalArgumentException("Method: " + method + "@" + extAnnotationClass + " must be used.");
                }
                return null;
            }
            final var foundRAM = AnnotationUtils.findAnnotation(found.getClass(), RAM.class);
            Preconditions.checkNotNull(foundRAM, "Ram extension must be use " + RAM.class + ", ref: " + RAMAllowAnonymous.class);
            return found;
        }

        public boolean isAllowAnonymous() {
            return findRAMExtension(RAMAllowAnonymous.class) != null;
        }

        public RAMDeclare getRAMDeclare() {
            return getRAMExtension(RAMDeclare.class);
        }

        public MethodDescriptor<?, ?> getMethodDesc() {
            return methodDesc;
        }

        public Method getMethod() {
            return method;
        }
    }

}
