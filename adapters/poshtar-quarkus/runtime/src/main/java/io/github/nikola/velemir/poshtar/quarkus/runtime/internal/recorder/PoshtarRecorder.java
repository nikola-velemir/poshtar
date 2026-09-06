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

package io.github.nikola.velemir.poshtar.quarkus.runtime.internal.recorder;

import io.github.nikola.velemir.poshtar.quarkus.runtime.internal.registry.QuarkusNotificationRegistry;
import io.github.nikola.velemir.poshtar.quarkus.runtime.internal.registry.QuarkusRequestRegistry;
import io.github.nikola_velemir.poshtar.core.pipeline.behaviour.PipelineBehaviour;
import io.github.nikola_velemir.poshtar.core.pipeline.configuration.PipelineConfiguration;
import io.quarkus.arc.Arc;
import io.quarkus.runtime.annotations.Recorder;
import jakarta.enterprise.inject.spi.BeanManager;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Core Quarkus recorder responsible for wiring and initializing PoshtaR registries at runtime.
 *
 * <p>
 * This recorder executes during the {@code RUNTIME_INIT} phase. It consumes metadata
 * collected during the deployment/build phase and maps them to
 * active runtime CDI beans, setting up request handlers, notification handlers, and pipeline behaviours.
 * </p>
 *
 * @author Nikola Velemir
 * @since 1.0.0
 */
@Recorder
public class PoshtarRecorder {
    /**
     * Initializes the request and notification registries by fetching active CDI containers
     * and resolving metadata maps built during deployment into live runtime class definitions.
     *
     * @param handlerToRequest                  map linking request handler class names to their corresponding request class names
     * @param notificationHandlerToNotification map linking notification handler class names to their corresponding notification class names
     * @param handlerToBehaviours               map linking request handler class names to their behaviour class names
     */
    public void initRegistries(
            Map<String, String> handlerToRequest,
            Map<String, String> notificationHandlerToNotification,
            Map<String, List<String>> handlerToBehaviours) {

        BeanManager bm = Arc.container().beanManager();

        QuarkusRequestRegistry requestRegistry = Arc.container()
                .instance(QuarkusRequestRegistry.class).get();
        QuarkusNotificationRegistry notificationRegistry = Arc.container()
                .instance(QuarkusNotificationRegistry.class).get();

        PipelineConfiguration pipelineConfiguration = Arc.container()
                .instance(PipelineConfiguration.class).get();

        List<Class<? extends PipelineBehaviour<?, ?>>> behaviourClasses = Collections.emptyList();
        if (pipelineConfiguration != null)
            behaviourClasses = pipelineConfiguration.getBehaviourClasses();

        List<Class<? extends PipelineBehaviour<?, ?>>> orderedBehaviourClasses = behaviourClasses;
        Map<String, PipelineBehaviour<?, ?>> behaviourProxies = ProxyUtility.extractBehaviourProxies(orderedBehaviourClasses, bm);


        MappingRegistrar.registerRequestMappings(handlerToRequest, handlerToBehaviours, orderedBehaviourClasses, behaviourProxies, requestRegistry, bm);
        MappingRegistrar.registerNotificationMappings(notificationHandlerToNotification, notificationRegistry, bm);
    }


}