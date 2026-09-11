package io.github.nikola_velemir.poshtar.micronaut.adapter.internal.registry;

import io.github.nikola_velemir.poshtar.core.pipeline.behaviour.PipelineBehaviour;
import io.github.nikola_velemir.poshtar.core.pipeline.configuration.PipelineConfiguration;
import io.github.nikola_velemir.poshtar.core.request.Request;
import io.github.nikola_velemir.poshtar.core.request.handler.RequestHandler;
import io.github.nikola_velemir.poshtar.core.request.registry.AbstractRequestRegistry;
import io.micronaut.context.BeanContext;
import io.micronaut.inject.BeanDefinition;

import java.util.Collection;
import java.util.List;

public class MicronautRequestRegistry extends AbstractRequestRegistry {
    private final BeanContext context;

    /**
     * Instantiates the registry, with the provided Micronaut context.
     *
     * @param context               Micronaut context, used for Poshtar component
     *                              discovery.
     * @param pipelineConfiguration Provided order of behavior execution.
     */
    public MicronautRequestRegistry(BeanContext context, PipelineConfiguration pipelineConfiguration) {
        this.context = context;
        init(pipelineConfiguration);
    }

    @SuppressWarnings("unchecked")
    private void init(PipelineConfiguration pipelineConfiguration) {
        List<PipelineBehaviour<?, ?>> orderedBehaviours = (List<PipelineBehaviour<?, ?>>) provideBehaviours(
                pipelineConfiguration);

        Collection<BeanDefinition<RequestHandler>> handlerDefinitions = context
                .getBeanDefinitions(RequestHandler.class);

        for (BeanDefinition<RequestHandler> definition : handlerDefinitions) {
            Class<?> requestType = resolveRequestType(definition);

            if (requestType == null || !Request.class.isAssignableFrom(requestType))
                continue;

            RequestHandler<?, ?> handler = context.getBean(definition);
            List<PipelineBehaviour<?, ?>> filteredBehaviours = filterBehaviours(orderedBehaviours, requestType);

            registerAsCasted(handler, requestType, filteredBehaviours);
        }
    }

    private static Class<?> resolveRequestType(BeanDefinition<RequestHandler> definition) {
        List<io.micronaut.core.type.Argument<?>> typeArguments = definition.getTypeArguments(RequestHandler.class);

        // 1. Standard Micronaut resolution (works perfectly for real beans)
        if (typeArguments != null && !typeArguments.isEmpty()) {
            return typeArguments.get(0).getType();
        }

        Class<?> beanType = definition.getBeanType();
        while (beanType != null && beanType != Object.class) {
            for (java.lang.reflect.Type genericInterface : beanType.getGenericInterfaces()) {
                if (genericInterface instanceof java.lang.reflect.ParameterizedType) {
                    java.lang.reflect.ParameterizedType paramType = (java.lang.reflect.ParameterizedType) genericInterface;
                    if (RequestHandler.class.equals(paramType.getRawType())) {
                        return (Class<?>) paramType.getActualTypeArguments()[0];
                    }
                }
            }
            beanType = beanType.getSuperclass();
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private void registerAsCasted(RequestHandler<?, ?> handler, Class<?> requestType,
            List<PipelineBehaviour<?, ?>> filteredBehaviours) {
        Class<Request<Object>> castedRequest = (Class<Request<Object>>) requestType;
        RequestHandler<Request<Object>, Object> castedHandler = (RequestHandler<Request<Object>, Object>) handler;

        register(castedRequest, castedHandler, filteredBehaviours);
    }

    private List<? extends PipelineBehaviour<?, ?>> provideBehaviours(PipelineConfiguration pipelineConfiguration) {
        return pipelineConfiguration
                .getBehaviourClasses()
                .stream()
                .map(context::getBean)
                .map(b -> (PipelineBehaviour<?, ?>) b)
                .toList();
    }

    @Override
    protected boolean supportsRequest(PipelineBehaviour<?, ?> behaviour, Class<?> requestType) {
        BeanDefinition<? extends PipelineBehaviour> definition = context.getBeanDefinition(behaviour.getClass());

        Class<?> genericRequestType = definition.getTypeArguments(PipelineBehaviour.class)
                .get(0)
                .getType();

        return genericRequestType != null && genericRequestType.isAssignableFrom(requestType);
    }
}
