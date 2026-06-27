package io.github.nikola.velemir.poshtar.quarkus.runtime;

import io.github.nikola_velemir.poshtar.core.pipeline.behaviour.PipelineBehaviour;
import io.github.nikola_velemir.poshtar.core.pipeline.configuration.PipelineConfiguration;
import io.github.nikola_velemir.poshtar.core.request.Request;
import io.github.nikola_velemir.poshtar.core.request.handler.RequestHandler;
import io.github.nikola_velemir.poshtar.core.request.registry.AbstractRequestRegistry;
import io.quarkus.arc.Arc;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Any;
import jakarta.enterprise.inject.Instance;
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
                    try {
                        return (PipelineBehaviour<?, ?>) clazz.getDeclaredConstructor().newInstance();
                    } catch (Exception e) {
                        throw new RuntimeException("Could not instantiate behaviour class: " + clazz.getName(), e);
                    }
                })
                .toList();
    }
    @Override
    protected boolean supportsRequest(PipelineBehaviour<?, ?> behaviour, Class<?> requestType) {
        for (Type iface : behaviour.getClass().getGenericInterfaces()) {
            if (!(iface instanceof ParameterizedType pt)) continue;
            if (!pt.getRawType().equals(PipelineBehaviour.class)) continue;

            Type arg = pt.getActualTypeArguments()[0];
            if (arg instanceof Class<?> genericRequestType) {
                return genericRequestType.isAssignableFrom(requestType);
            }
            // TypeVariable (e.g. TRequest extends Request<TResponse>) — global behaviour, supports everything
            return true;
        }
        return false;
    }
}