package io.github.nikola.velemir.poshtar.quarkus.runtime;

import io.github.nikola_velemir.poshtar.core.pipeline.behaviour.PipelineBehaviour;
import io.github.nikola_velemir.poshtar.core.pipeline.configuration.PipelineConfiguration;
import io.github.nikola_velemir.poshtar.core.request.Request;
import io.github.nikola_velemir.poshtar.core.request.handler.RequestHandler;
import io.github.nikola_velemir.poshtar.core.request.registry.AbstractRequestRegistry;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.spi.Bean;
import jakarta.enterprise.inject.spi.BeanManager;
import jakarta.inject.Inject;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
@ApplicationScoped
public class QuarkusRequestRegistry extends AbstractRequestRegistry {

    @Inject
    PipelineConfiguration pipelineConfiguration;

    @SuppressWarnings({"rawtypes", "unchecked"})
    public void registerFromClass(Class<?> beanClass, List<PipelineBehaviour<?, ?>> allBehaviours, BeanManager bm) {
        Bean<?> bean = bm.getBeans(beanClass).stream().findFirst()
                .orElseThrow(() -> new RuntimeException("No CDI bean for handler: " + beanClass.getName()));
        RequestHandler handler = (RequestHandler) bm.getReference(
                bean, beanClass, bm.createCreationalContext(bean));

        Class<?> requestType = extractRequestType(beanClass);
        if (requestType == null) return;

        List<PipelineBehaviour<?, ?>> filtered = filterBehaviours(allBehaviours, requestType);
        register((Class) requestType, handler, filtered);
        System.out.println("[PoshtaR] Registered handler: " + beanClass.getSimpleName());
    }

    @Override
    protected boolean supportsRequest(PipelineBehaviour<?, ?> behaviour, Class<?> requestType) {
        Class<?> clazz = behaviour.getClass();
        while (clazz != null && clazz != Object.class) {
            for (Type iface : clazz.getGenericInterfaces()) {
                if (!(iface instanceof ParameterizedType pt)) continue;
                if (!pt.getRawType().toString().contains(PipelineBehaviour.class.getSimpleName())) continue;
                Type arg = pt.getActualTypeArguments()[0];
                if (arg instanceof Class<?> genericRequestType) {
                    return genericRequestType.isAssignableFrom(requestType);
                }
                return true;
            }
            clazz = clazz.getSuperclass();
        }
        return false;
    }

    private Class<?> extractRequestType(Class<?> clazz) {
        Class<?> current = clazz;
        while (current != null && current != Object.class) {
            for (Type iface : current.getGenericInterfaces()) {
                if (!(iface instanceof ParameterizedType pt)) continue;
                if (!pt.getRawType().toString().contains(RequestHandler.class.getSimpleName())) continue;
                Type arg = pt.getActualTypeArguments()[0];
                if (arg instanceof Class<?> reqClass && Request.class.isAssignableFrom(reqClass)) {
                    return reqClass;
                }
            }
            current = current.getSuperclass();
        }
        return null;
    }
}