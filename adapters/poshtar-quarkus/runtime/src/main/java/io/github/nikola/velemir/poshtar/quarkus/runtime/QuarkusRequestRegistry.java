package io.github.nikola.velemir.poshtar.quarkus.runtime;

import io.github.nikola_velemir.poshtar.core.pipeline.behaviour.PipelineBehaviour;
import io.github.nikola_velemir.poshtar.core.pipeline.configuration.PipelineConfiguration;
import io.github.nikola_velemir.poshtar.core.request.Request;
import io.github.nikola_velemir.poshtar.core.request.handler.RequestHandler;
import io.github.nikola_velemir.poshtar.core.request.registry.AbstractRequestRegistry;
import io.quarkus.arc.Arc;
import io.quarkus.arc.InstanceHandle;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

@ApplicationScoped
public class QuarkusRequestRegistry extends AbstractRequestRegistry {


    @Inject
    PipelineConfiguration pipelineConfiguration;

    @SuppressWarnings({"rawtypes", "unchecked"})
    public void initFromClassNames(List<String> classNames, List<String> behaviourClassNames) {
        List<? extends PipelineBehaviour<?, ?>> orderedBehaviours = provideBehaviours(behaviourClassNames);
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        for (String className : classNames) {
            try {
                Class<?> beanClass = Class.forName(className, true, classLoader);
                RequestHandler handler = (RequestHandler) Arc.container()
                        .instance(beanClass)
                        .get();

                if (handler == null) continue;

                for (Type iface : beanClass.getGenericInterfaces()) {
                    if (!(iface instanceof ParameterizedType pt)) continue;
                    if (!pt.getRawType().equals(RequestHandler.class)) continue;

                    Type arg = pt.getActualTypeArguments()[0];
                    if (arg instanceof Class<?> requestType
                            && Request.class.isAssignableFrom(requestType)) {
                        List<PipelineBehaviour<?, ?>> filtered =
                                filterBehaviours((List<PipelineBehaviour<?, ?>>) orderedBehaviours, requestType);
                        register((Class) requestType, handler, filtered);
                    }
                    break;
                }
            } catch (ClassNotFoundException e) {
                throw new RuntimeException("Could not load handler class: " + className, e);
            }
        }
    }

    private List<? extends PipelineBehaviour<?, ?>> provideBehaviours(List<String> behaviourClassNames) {
        return pipelineConfiguration.getBehaviourClasses()
                .stream()
                .filter(clazz -> behaviourClassNames.contains(clazz.getName()))
                .map(clazz -> {
                    InstanceHandle<?> handle = Arc.container().instance(clazz);
                    if (!handle.isAvailable()) {
                        throw new RuntimeException("No CDI bean found for behaviour class: " + clazz.getName());
                    }
                    return (PipelineBehaviour<?, ?>) handle.get();
                })
                .toList();
    }
    @Override
    protected boolean supportsRequest(PipelineBehaviour<?, ?> behaviour, Class<?> requestType) {
        Class<?> clazz = behaviour.getClass();
        while (clazz != null && clazz != Object.class) {
            for (Type iface : clazz.getGenericInterfaces()) {
                if (!(iface instanceof ParameterizedType pt)) continue;
                if (!pt.getRawType().equals(PipelineBehaviour.class)) continue;
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