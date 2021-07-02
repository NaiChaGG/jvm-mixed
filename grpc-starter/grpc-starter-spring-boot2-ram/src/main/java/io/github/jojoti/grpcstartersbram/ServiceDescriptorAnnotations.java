package io.github.jojoti.grpcstartersbram;

import com.google.common.collect.ImmutableMap;
import io.grpc.MethodDescriptor;
import io.grpc.ServiceDescriptor;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;

/**
 * @author Wang Yue
 */
interface ServiceDescriptorAnnotations {

    static <T extends Annotation> ImmutableMap<MethodDescriptor<?, ?>, T> getAnnotationMaps(
            List<ServiceDescriptor> servicesEvent, Class<T> t, boolean forced) {
        var builder = ImmutableMap.<MethodDescriptor<?, ?>, T>builder();

        for (ServiceDescriptor serviceDescriptor : servicesEvent) {
            for (MethodDescriptor<?, ?> method : serviceDescriptor.getMethods()) {
                try {
                    var services = Class.forName(method.getServiceName());

                    var foundMethodName = method.getFullMethodName().substring(method.getServiceName().length());

                    for (Method servicesMethod : services.getMethods()) {
                        if (servicesMethod.getName().equals(foundMethodName)) {
                            var foundAnnotations = AnnotationUtils.getAnnotation(servicesMethod, t);
                            if (foundAnnotations != null) {
                                builder.put(method, foundAnnotations);
                            } else {
                                if (forced) {
                                    throw new IllegalArgumentException("Annotation: @" + t.getPackageName() + "." + t.getSimpleName() + " must be used, method : " + servicesMethod);
                                }
                            }
                        }
                    }
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }

            }
        }
        return builder.build();
    }

}
