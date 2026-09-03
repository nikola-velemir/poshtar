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
import jakarta.enterprise.inject.spi.BeanManager;

import java.util.List;
import java.util.Map;
/**
 * Class responsible for decoding build-time string metadata
 * into executable runtime classes and registering them into their respective PoshtaR registries.
 *
 * @author Nikola Velemir
 * @since 1.0.0
 */
final class MappingRegistrar {
    /**
     * Iterates over discovered request handlers, resolves their specific class definitions
     * using the current thread context class loader, filters applicable behaviours according
     * to global ordering rules, and binds them to the request registry topology.
     *
     * @param handlerToRequest map of request handler class names to request class names
     * @param handlerToBehaviours map of request handler class names to their applicable behaviour class names
     * @param orderedBehaviourClasses the globally defined, ordered list of behaviour class keys
     * @param behaviourProxies contextual CDI references matching behaviour names to live proxies
     * @param requestRegistry the destination registry where request paths are registered
     * @param bm the active CDI {@link BeanManager} used to fulfill registration requirements
     */
    @SuppressWarnings("unchecked")
    static void registerRequestMappings(
            Map<String, String> handlerToRequest,
            Map<String, List<String>> handlerToBehaviours,
            List<Class<? extends PipelineBehaviour<?, ?>>> orderedBehaviourClasses,
            Map<String, PipelineBehaviour<?, ?>> behaviourProxies,
            QuarkusRequestRegistry requestRegistry,
            BeanManager bm) {

        handlerToRequest.forEach((handlerName, requestName) -> {
            Class<?> handlerClass = ClassLoadingUtility.loadClass(handlerName);
            Class<?> requestClass = ClassLoadingUtility.loadClass(requestName);


            List<String> applicableBehaviourNames = handlerToBehaviours.getOrDefault(handlerName, List.of());
            List<? extends PipelineBehaviour<?, ?>> filteredBehaviours = orderedBehaviourClasses.stream()
                    .filter(bc -> applicableBehaviourNames.contains(bc.getName()))
                    .map(bc -> behaviourProxies.get(bc.getName()))
                    .toList();

            requestRegistry.registerFromClass(handlerClass, requestClass, (List<PipelineBehaviour<?, ?>>) filteredBehaviours, bm);
        });
    }
    /**
     * Iterates over discovered notification handlers, maps them to their respective
     * notification types dynamically, and registers them directly into the execution landscape.
     *
     * @param notificationHandlerToNotification map linking notification handler class names to notification class names
     * @param notificationRegistry the destination registry where notification routes are registered
     * @param bm the active CDI {@link BeanManager} used to fulfill registration requirements
     */
    static void registerNotificationMappings(
            Map<String, String> notificationHandlerToNotification,
            QuarkusNotificationRegistry notificationRegistry,
            BeanManager bm) {

        notificationHandlerToNotification.forEach((handlerName, notifName) -> {
            Class<?> handlerClass = ClassLoadingUtility.loadClass(handlerName);
            Class<?> notifClass = ClassLoadingUtility.loadClass(notifName);
            notificationRegistry.registerFromClass(handlerClass, notifClass, bm);
        });
    }


}
