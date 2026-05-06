package io.github.nikola_velemir.poshtar.adapter.configuration;


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
public class PipelineConfigurer {
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
    public <T extends PipelineBehaviour<?, ?>> PipelineConfigurer add(Class<T> behaviourClass) {
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
