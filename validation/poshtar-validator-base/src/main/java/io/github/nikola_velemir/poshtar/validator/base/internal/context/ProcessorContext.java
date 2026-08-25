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

package io.github.nikola_velemir.poshtar.validator.base.internal.context;

import com.sun.source.util.Trees;
import io.github.nikola_velemir.poshtar.validator.base.internal.registry.RegistryEntry;
import io.github.nikola_velemir.poshtar.validator.base.internal.rules.Rule;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import java.util.*;
import java.util.stream.Collectors;

/**
 * A shared state container used during the annotation processing lifecycle.
 * <p>
 * Aggregates information discovered during classpath scanning,
 * such as registered handlers, requests, and behaviors. It provides unified access for
 * {@link Rule} implementations to
 * perform cross-component validation.
 * </p>
 *
 * @author Nikola Velemir
 * @version ${project.version}
 * @since 1.0.0
 */
public class ProcessorContext {
    public final ProcessingEnvironment env;
    public final Trees trees;
    private final Map<String, RegistryEntry> handlerRegistry = new LinkedHashMap<>();
    private final Set<String> knownRequests = new HashSet<>();
    private final Set<String> knownNotifications = new HashSet<>();
    /**
     * Constructs a context with an explicit Trees instance.
     *
     * @param env   The current processing environment.
     * @param trees The Trees utility (often unwrapped for IDE compatibility).
     */
    public ProcessorContext(ProcessingEnvironment env, Trees trees) {
        this.env = env;
        this.trees = trees;
    }

    /**
     * Constructs a context and initializes the Trees instance from the environment.
     *
     * @param env The current processing environment.
     */
    public ProcessorContext(ProcessingEnvironment env) {
        this.env = env;
        this.trees = Trees.instance(env);
    }

    /**
     * Registers a request type found in the source code.
     *
     * @param requestFqn The fully qualified name of the request class.
     */
    public void registerRequest(String requestFqn) {
        knownRequests.add(requestFqn);
    }
    /**
     * Registers a notification type found in the source code.
     *
     * @param notificationFqn The fully qualified name of the notification class.
     */
    public void registerNotification(String notificationFqn){
        knownNotifications.add(notificationFqn);
    }
    /**
     * Returns an unmodifiable view of the handler registry.
     *
     * @return A map of handler names to their respective registry entries.
     */
    public Map<String, RegistryEntry> getHandlerRegistry() {
        return Collections.unmodifiableMap(handlerRegistry);
    }

    /**
     * Provides {@link Elements} utility to the client class.
     *
     * @return The standard Element utility for inspecting Java structures.
     */
    public Elements getElements() {
        return env.getElementUtils();
    }

    /**
     * Provides {@link Types} utility to the client class.
     *
     * @return The standard Type utility for type-system comparisons.
     */
    public Types getTypes() {
        return env.getTypeUtils();
    }

    /**
     * Adds a handler to the registry and associates it with a Request type.
     *
     * @param handlerFqn     The fully qualified name of the handler.
     * @param requestFqn     The fully qualified name of the request it handles.
     * @param handlerElement The compile-time element representing the handler class.
     * @param mirror         The annotation mirror for the @Handler annotation.
     */
    public void registerHandler(String handlerFqn, String requestFqn,
                                Element handlerElement, AnnotationMirror mirror) {
        handlerRegistry.put(handlerFqn, new RegistryEntry(requestFqn, handlerFqn, handlerElement, mirror));
    }

    /**
     * Adds a Pipeline Behavior to the registry.
     *
     * @param behaviourFqn     The fully qualified name of the behavior.
     * @param behaviourElement The compile-time element representing the behavior class.
     * @param mirror           The annotation mirror for the @Behaviour annotation.
     */
    public void registerBehaviour(String behaviourFqn, Element behaviourElement, AnnotationMirror mirror) {
        handlerRegistry.put(behaviourFqn, new RegistryEntry("BEHAVIOUR", behaviourFqn, behaviourElement, mirror));

    }

    /**
     * Filters the registry to return only the FQNs of handled requests.
     *
     * @return A set of request class names that have at least one handler.
     */
    public Set<String> getHandledRequestTypes() {
        return handlerRegistry.values().stream()
                .filter(e -> !e.isBehaviour())
                .map(RegistryEntry::requestFQN)
                .collect(Collectors.toSet());
    }

    /**
     * Filters the registry to return only the FQNs of pipeline behaviors.
     *
     * @return A set of behavior class names.
     */
    public Set<String> getKnownBehaviours() {
        return handlerRegistry.values().stream()
                .filter(RegistryEntry::isBehaviour)
                .map(RegistryEntry::handlerFQN)
                .collect(Collectors.toSet());
    }

    /**
     * Provides a set of all registered handler FQNs to the client class.
     *
     * @return A set containing all registered handler and behavior class names.
     */
    public Set<String> getAll() {
        return handlerRegistry.values().stream()
                .map(RegistryEntry::handlerFQN)
                .collect(Collectors.toSet());
    }

    /**
     * Provides a set of found request FQNs to the client class.
     *
     * @return An unmodifiable view of all discovered request types.
     */
    public Set<String> getKnownRequests() {
        return Collections.unmodifiableSet(knownRequests);
    }
    /**
     * Provides a set of found notification FQNs to the client class.
     *
     * @return An unmodifiable view of all discovered notification types.
     */
    public Set<String> getKnownNotifications() {
        return Collections.unmodifiableSet(knownNotifications);
    }

}