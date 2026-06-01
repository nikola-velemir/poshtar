/*
 * Copyright (C) 2026 Nikola (nvelem.nikola@gmail.com)
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */

package io.github.nikola_velemir.poshtar.spring.adapter.internal.registry;


import io.github.nikola_velemir.poshtar.core.pipeline.behaviour.PipelineBehaviour;
import io.github.nikola_velemir.poshtar.core.pipeline.configuration.PipelineConfiguration;
import io.github.nikola_velemir.poshtar.core.request.Request;
import io.github.nikola_velemir.poshtar.core.request.handler.RequestHandler;
import io.github.nikola_velemir.poshtar.core.request.registry.AbstractRequestRegistry;
import org.springframework.aop.support.AopUtils;
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
    private volatile boolean initialized = false;

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
            Class<?> targetClass = AopUtils.getTargetClass(handler);
            Class<?> requestType = ResolvableType.forClass(targetClass)
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
        ResolvableType behaviourInterface = ResolvableType.forClass(AopUtils.getTargetClass(behaviour))
                .as(PipelineBehaviour.class);
        Class<?> genericRequestType = behaviourInterface.getGeneric(0).resolve();
        if (genericRequestType == null) {
            return behaviourInterface.getGeneric(0).isAssignableFrom(requestType);
        }
        return genericRequestType.isAssignableFrom(requestType);
    }


    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (event.getApplicationContext() == context && !initialized) {
            initialized = true;
            init(context);
        }
    }
}
