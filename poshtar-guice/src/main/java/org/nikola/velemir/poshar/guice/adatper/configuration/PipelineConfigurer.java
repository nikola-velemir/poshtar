package org.nikola.velemir.poshar.guice.adatper.configuration;

import org.nikola.velemir.poshtar.core.pipeline.behaviour.PipelineBehaviour;

import java.util.ArrayList;
import java.util.List;

public class PipelineConfigurer {
    private final List<Class<? extends PipelineBehaviour<?, ?>>> behaviourClasses =
            new ArrayList<>();

    public <T extends PipelineBehaviour<?, ?>> PipelineConfigurer add(Class<T> behaviourClass) {
        if (behaviourClass == null) {
            throw new IllegalArgumentException("Behaviour class cannot be null");
        }
        this.behaviourClasses.add(behaviourClass);
        return this;
    }

    public List<Class<? extends PipelineBehaviour<?, ?>>> getBehaviourClasses() {
        return behaviourClasses;
    }
}
