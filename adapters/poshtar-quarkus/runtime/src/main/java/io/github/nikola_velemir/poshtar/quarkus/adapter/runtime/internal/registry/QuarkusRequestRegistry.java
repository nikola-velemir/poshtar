package io.github.nikola_velemir.poshtar.quarkus.adapter.runtime.internal.registry;

import io.github.nikola_velemir.poshtar.core.pipeline.behaviour.PipelineBehaviour;
import io.github.nikola_velemir.poshtar.core.pipeline.configuration.PipelineConfiguration;
import io.github.nikola_velemir.poshtar.core.request.Request;
import io.github.nikola_velemir.poshtar.core.request.handler.RequestHandler;
import io.github.nikola_velemir.poshtar.core.request.registry.AbstractRequestRegistry;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.enterprise.inject.Instance;
import jakarta.enterprise.inject.spi.Bean;
import jakarta.enterprise.inject.spi.BeanManager;
import jakarta.inject.Inject;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

@ApplicationScoped
public class QuarkusRequestRegistry extends AbstractRequestRegistry {

    @Inject
    @SuppressWarnings("rawtypes")
    Instance<RequestHandler> handlers;

    @Inject
    Instance<PipelineBehaviour<?, ?>> behaviours;

    @Inject
    PipelineConfiguration pipelineConfiguration;

    @Inject
    BeanManager beanManager;

    @SuppressWarnings({"rawtypes", "unchecked"})
    void init(@Observes StartupEvent event) {
        List<? extends PipelineBehaviour<?, ?>> orderedBehaviours = provideBehaviours();

        for (RequestHandler handler : handlers) {
            Bean<?> bean = beanManager.resolve(
                    beanManager.getBeans(handler.getClass())
            );

            Type requestHandlerType = bean.getTypes().stream()
                    .filter(t -> t instanceof ParameterizedType pt
                            && pt.getRawType().equals(RequestHandler.class))
                    .findFirst()
                    .orElse(null);

            if (requestHandlerType instanceof ParameterizedType pt) {
                Type arg = pt.getActualTypeArguments()[0];
                if (arg instanceof Class<?> requestType
                        && Request.class.isAssignableFrom(requestType)) {

                    List<PipelineBehaviour<?, ?>> filtered =
                            filterBehaviours((List<PipelineBehaviour<?, ?>>) orderedBehaviours, requestType);

                    register(
                            (Class<Request<Object>>) requestType,
                            (RequestHandler<Request<Object>, Object>) handler,
                            filtered
                    );
                }
            }
        }
    }

    @Override
    protected boolean supportsRequest(PipelineBehaviour<?, ?> behaviour, Class<?> requestType) {
        Bean<?> bean = beanManager.resolve(
                beanManager.getBeans(behaviour.getClass())
        );

        Type behaviourType = bean.getTypes().stream()
                .filter(t -> t instanceof ParameterizedType pt
                        && pt.getRawType().equals(PipelineBehaviour.class))
                .findFirst()
                .orElse(null);

        if (behaviourType instanceof ParameterizedType pt) {
            Type arg = pt.getActualTypeArguments()[0];
            if (arg instanceof Class<?> genericRequestType) {
                return genericRequestType.isAssignableFrom(requestType);
            }
            // wildcard or type variable — assume it supports everything
            return true;
        }
        return false;
    }

    private List<? extends PipelineBehaviour<?, ?>> provideBehaviours() {
        return pipelineConfiguration
                .getBehaviourClasses()
                .stream()
                .map(clazz -> behaviours.select(clazz).get())
                .toList();
    }
}