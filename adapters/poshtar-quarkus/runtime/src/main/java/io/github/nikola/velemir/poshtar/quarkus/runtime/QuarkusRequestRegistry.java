package io.github.nikola.velemir.poshtar.quarkus.runtime;

import io.github.nikola_velemir.poshtar.core.pipeline.behaviour.PipelineBehaviour;
import io.github.nikola_velemir.poshtar.core.pipeline.configuration.PipelineConfiguration;
import io.github.nikola_velemir.poshtar.core.request.Request;
import io.github.nikola_velemir.poshtar.core.request.handler.RequestHandler;
import io.github.nikola_velemir.poshtar.core.request.registry.AbstractRequestRegistry;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.spi.CreationalContext;
import jakarta.enterprise.event.Observes;
import jakarta.enterprise.inject.Any;
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

    @Inject
    BeanManager beanManager;

    @SuppressWarnings({"rawtypes", "unchecked"})
    void init(@Observes StartupEvent event) {
        List<? extends PipelineBehaviour<?, ?>> orderedBehaviours = provideBehaviours();
        int handlerCount = 0;

        for (Bean<?> bean : beanManager.getBeans(Object.class, Any.Literal.INSTANCE)) {
            Class<?> beanClass = bean.getBeanClass();

            if (!implementsInterface(beanClass, RequestHandler.class.getName())) continue;

            CreationalContext<?> ctx = beanManager.createCreationalContext(bean);
            RequestHandler handler = (RequestHandler) beanManager.getReference(bean, beanClass, ctx);

            Class<?> requestType = extractRequestType(beanClass);
            if (requestType == null) continue;

            List<PipelineBehaviour<?, ?>> filtered =
                    filterBehaviours((List<PipelineBehaviour<?, ?>>) orderedBehaviours, requestType);

            register((Class) requestType, handler, filtered);
            handlerCount++;
            System.out.println("[PoshtaR] Registered handler: " + beanClass.getSimpleName());
        }

        System.out.println("[PoshtaR] Registry initialization complete. Total handlers: " + handlerCount);
    }

    private boolean implementsInterface(Class<?> clazz, String interfaceName) {
        while (clazz != null && clazz != Object.class) {
            for (Class<?> iface : clazz.getInterfaces()) {
                if (iface.getName().equals(interfaceName)) return true;
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
                if (arg instanceof Class<?> reqClass
                        && Request.class.isAssignableFrom(reqClass)) {
                    return reqClass;
                }
            }
            current = current.getSuperclass();
        }
        return null;
    }

    private List<? extends PipelineBehaviour<?, ?>> provideBehaviours() {
        return pipelineConfiguration.getBehaviourClasses()
                .stream()
                .map(clazz -> {
                    Bean<?> bean = beanManager.getBeans(clazz)
                            .stream()
                            .findFirst()
                            .orElseThrow(() -> new RuntimeException(
                                    "No CDI bean found for behaviour class: " + clazz.getName()));
                    CreationalContext<?> ctx = beanManager.createCreationalContext(bean);
                    return (PipelineBehaviour<?, ?>) beanManager.getReference(bean, clazz, ctx);
                })
                .toList();
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
                return true; // TypeVariable = global behaviour
            }
            clazz = clazz.getSuperclass();
        }
        return false;
    }
}