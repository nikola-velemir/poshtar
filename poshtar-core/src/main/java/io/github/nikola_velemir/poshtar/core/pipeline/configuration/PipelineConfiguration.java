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
