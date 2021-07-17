package io.github.jojoti.grpcstartersbram;

import com.google.common.collect.ImmutableList;
import io.grpc.BindableService;
import io.grpc.MethodDescriptor;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;

/**
 * @author JoJo Wang
 * @link github.com/jojoti
 */
interface ServiceDescriptorAnnotations {

    final class ServiceMethods<T extends Annotation> {
        final BindableService object;
        final ImmutableList<ServiceMethod<T>> methods;

        ServiceMethods(BindableService object, ImmutableList<ServiceMethod<T>> methods) {
            this.object = object;
            this.methods = methods;
        }
    }

    final class ServiceMethod<T extends Annotation> {
        final MethodDescriptor<?, ?> methodDescriptor;
        final Method method;
        final T foundAnnotation;

        ServiceMethod(MethodDescriptor<?, ?> methodDescriptor, Method method, T foundAnnotation) {
            this.methodDescriptor = methodDescriptor;
            this.method = method;
            this.foundAnnotation = foundAnnotation;
        }

    }

    static <T extends Annotation> ImmutableList<ServiceMethods<T>> getAnnotationMapsV2(List<BindableService> serviceObjects, Class<T> t, boolean forced) {
        final var builder = ImmutableList.<ServiceMethods<T>>builder();
        for (var serviceObject : serviceObjects) {
            final var methods = ImmutableList.<ServiceMethod<T>>builder();
            for (var methodObject : serviceObject.bindService().getMethods()) {
                for (Method method1 : serviceObject.getClass().getMethods()) {
                    // 定义到 pb 里面的 代码
                    if (method1.getName().equals(methodObject.getMethodDescriptor().getBareMethodName())) {
                        var foundAnnotations = AnnotationUtils.getAnnotation(method1, t);
                        if (foundAnnotations != null) {
//                            builder.put(methodObject.getMethodDescriptor(), foundAnnotations);
                            methods.add(new ServiceMethod<>(methodObject.getMethodDescriptor(), method1, foundAnnotations));
                        } else {
                            if (forced) {
                                throw new IllegalArgumentException("Method: " + methodObject.getMethodDescriptor() + "@" + t.getPackageName() + "." + t.getSimpleName() + " must be used");
                            }
                        }
                    }
                }
            }
            builder.add(new ServiceMethods<>(serviceObject, methods.build()));
        }
        return builder.build();
    }

}
