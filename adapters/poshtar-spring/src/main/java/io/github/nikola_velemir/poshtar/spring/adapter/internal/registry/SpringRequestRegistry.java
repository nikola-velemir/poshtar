/*
 * Copyright 2026 Nikola Velemir
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.nikola_velemir.poshtar.spring.adapter.internal.registry;


import io.github.nikola_velemir.poshtar.adapter.configuration.PipelineConfiguration;
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

/**
 * Class maps specific request type to its chain of behaviors supported by generic constraint, ending with a handler.
 * Note that behavior and handler classes can be Spring proxies.
 *
 * @author Nikola Velemir
 * @version ${project.version}
 * @since 1.0.0
 */
public class SpringRequestRegistry extends AbstractRequestRegistry implements ApplicationListener<ContextRefreshedEvent> {
    private final ApplicationContext context;
    private final PipelineConfiguration pipelineConfiguration;
    /**
     * Instantiates the registry, with the provided Spring context.
     * @param context Spring context, used for Posthar component discovery.
     * @param pipelineConfiguration Provided order of behavior execution.
     */
    public SpringRequestRegistry(ApplicationContext context, PipelineConfiguration pipelineConfiguration) {
        this.context = context;
        this.pipelineConfiguration = pipelineConfiguration;
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
        return pipelineConfiguration
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
