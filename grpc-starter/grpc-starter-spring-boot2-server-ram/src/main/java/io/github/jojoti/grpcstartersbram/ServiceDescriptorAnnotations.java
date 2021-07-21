package io.github.jojoti.grpcstartersbram;

import com.google.common.base.Ascii;
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

    static <T extends Annotation> ImmutableList<ServiceMethods<T>> getAnnotationMapsV2(List<BindableService> serviceObjects, Class<T> t, boolean forced) {
        final var builder = ImmutableList.<ServiceMethods<T>>builder();
        for (var serviceObject : serviceObjects) {
            final var methods = ImmutableList.<ServiceMethod<T>>builder();

            final var foundClass = AnnotationUtils.getAnnotation(serviceObject.getClass(), t);

            for (var methodObject : serviceObject.bindService().getMethods()) {
                for (Method method1 : serviceObject.getClass().getMethods()) {
                    // 定义到 pb 里面的 代码
                    //
                    final var pbMethod = methodObject.getMethodDescriptor().getBareMethodName();
                    // copy com/google/common/base/CaseFormat.java:209
                    if ((Ascii.toUpperCase(pbMethod.charAt(0)) + Ascii.toLowerCase(pbMethod.substring(1))).equals(pbMethod)) {
                        throw new IllegalArgumentException("Method: " + pbMethod + " ,first char is not allow upper word");
                    }
                    if (method1.getName().equals(pbMethod)) {
                        var foundAnnotations = AnnotationUtils.getAnnotation(method1, t);
                        if (foundAnnotations != null || foundClass != null) {
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

}
