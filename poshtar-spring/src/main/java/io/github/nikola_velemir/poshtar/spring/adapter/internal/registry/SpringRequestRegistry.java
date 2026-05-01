package io.github.nikola_velemir.poshtar.spring.adapter.internal.registry;


import io.github.nikola_velemir.poshtar.adapter.configuration.PipelineConfigurer;
import io.github.nikola_velemir.poshtar.core.pipeline.behaviour.PipelineBehaviour;
import io.github.nikola_velemir.poshtar.core.request.Request;
import io.github.nikola_velemir.poshtar.core.request.handler.RequestHandler;
import io.github.nikola_velemir.poshtar.core.request.registry.AbstractRequestRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.ResolvableType;
import org.springframework.lang.NonNull;

import java.util.*;


public class SpringRequestRegistry extends AbstractRequestRegistry implements ApplicationListener<ContextRefreshedEvent> {
    private final ApplicationContext context;
    private final PipelineConfigurer pipelineConfigurer;

    public SpringRequestRegistry(ApplicationContext context, PipelineConfigurer pipelineConfigurer) {
        this.context = context;
        this.pipelineConfigurer = pipelineConfigurer;
    }

    @SuppressWarnings("unchecked")
    private void init(ApplicationContext context) {
        @SuppressWarnings("rawtypes") Map<String, RequestHandler> allHandlers = context.getBeansOfType(RequestHandler.class);
        List<? extends PipelineBehaviour<?, ?>> orderedBehaviours = provideBehaviours(context);

        for (RequestHandler<?, ?> handler : allHandlers.values()) {
            Class<?> requestType = ResolvableType.forClass(handler.getClass())
                    .as(RequestHandler.class)
                    .getGeneric(0).resolve();

            if (requestType == null || !Request.class.isAssignableFrom(requestType)) continue;

            List<PipelineBehaviour<?, ?>> filteredBehaviours = filterBehaviours((List<PipelineBehaviour<?, ?>>) orderedBehaviours, requestType);

            Class<Request<Object>> castedRequest = (Class<Request<Object>>) requestType;
            RequestHandler<Request<Object>, Object> castedHandler = (RequestHandler<Request<Object>, Object>) handler;

            register(castedRequest, castedHandler, filteredBehaviours);
        }

    }

    @NonNull
    private List<? extends PipelineBehaviour<?, ?>> provideBehaviours(ApplicationContext context) {
        return pipelineConfigurer
                .getBehaviourClasses()
                .stream()
                .map(context::getBean)
                .toList();
    }

    @Override
    protected boolean supportsRequest(PipelineBehaviour<?, ?> behaviour, Class<?> requestType) {
        ResolvableType behaviourInterface = ResolvableType.forClass(behaviour.getClass())
                .as(PipelineBehaviour.class);
        Class<?> genericRequestType = behaviourInterface.getGeneric(0).resolve();
        if (genericRequestType == null) {
            return behaviourInterface.getGeneric(0).isAssignableFrom(requestType);
        }
        return genericRequestType.isAssignableFrom(requestType);
    }


    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (event.getApplicationContext() == context) {
            init(context);
        }
    }
}
