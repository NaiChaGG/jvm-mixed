package io.github.jojoti.grpcstartersbram;

import com.google.common.collect.ImmutableMap;
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
public interface ServiceDescriptorAnnotations {

    static <T extends Annotation> ImmutableMap<MethodDescriptor<?, ?>, T> getAnnotationMaps(List<BindableService> serviceObjects, Class<T> t, boolean forced) {
        final var builder = ImmutableMap.<MethodDescriptor<?, ?>, T>builder();
        for (var serviceObject : serviceObjects) {
            for (var methodObject : serviceObject.bindService().getMethods()) {
                for (Method method1 : serviceObject.getClass().getMethods()) {
                    // 定义到 pb 里面的 代码
                    if (method1.getName().equals(methodObject.getMethodDescriptor().getBareMethodName())) {
                        var foundAnnotations = AnnotationUtils.getAnnotation(method1, t);
                        if (foundAnnotations != null) {
                            builder.put(methodObject.getMethodDescriptor(), foundAnnotations);
                        } else {
                            if (forced) {
                                throw new IllegalArgumentException("Annotation: @" + t.getPackageName() + "." + t.getSimpleName() + " must be used, method : " + methodObject.getMethodDescriptor());
                            }
                        }
                    }
                }
            }
        }
        return builder.build();
    }

}
