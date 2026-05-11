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

package io.github.nikola_velemir.poshtar.core.pipeline.configuration;


import io.github.nikola_velemir.poshtar.core.pipeline.behaviour.PipelineBehaviour;

import java.util.ArrayList;
import java.util.List;

/**
 * Configuration builder used to define and order the global pipeline behaviors.
 * <p>
 * Class allows developers to register {@link PipelineBehaviour} types.
 * The order in which behaviors are added to this configurer is
 * the order in which they will be executed when a request is dispatched.
 * </p>
 *
 * <p>Example usage:</p>
 * <pre>
 * configurer
 *     .add(LoggingBehaviour.class)
 *     .add(ValidationBehaviour.class);
 * </pre>
 *
 * @author Nikola Velemir
 * @version ${project.version}
 * @since 1.0.0
 */
public class PipelineConfiguration {
    /**
     * Internal list of behavior classes, maintaining the order of registration.
     */
    private final List<Class<? extends PipelineBehaviour<?, ?>>> behaviourClasses =
            new ArrayList<>();

    /**
     * Adds a behavior class to the end of the pipeline.
     * <p>
     * Behaviors added first will wrap behaviors added later.
     * </p>
     *
     * @param <T>            The specific type of the behavior.
     * @param behaviourClass The class literal of the behavior to add.
     * @return This configurer instance for method chaining.
     * @throws IllegalArgumentException if the provided class is null.
     */
    public <T extends PipelineBehaviour<?, ?>> PipelineConfiguration add(Class<T> behaviourClass) {
        if (behaviourClass == null) {
            throw new IllegalArgumentException("Behaviour class cannot be null");
        }
        this.behaviourClasses.add(behaviourClass);
        return this;
    }
    /**
     * Retrieves the ordered list of registered behavior classes.
     *
     * @return A list of classes representing the configured pipeline.
     */
    public List<Class<? extends PipelineBehaviour<?, ?>>> getBehaviourClasses() {
        return behaviourClasses;
    }
}
