package nikola.velemir.poshtar.spring.adapter.configuration;

import org.nikola.velemir.poshtar.core.pipeline.behaviour.PipelineBehaviour;

import java.util.ArrayList;
import java.util.List;

public class PipelineConfigurer {
    private final List<Class<? extends PipelineBehaviour<?, ?>>> behaviourClasses =
            new ArrayList<>();

    public PipelineConfigurer add(Class<? extends PipelineBehaviour<?,?>> behaviourClass){
        this.behaviourClasses.add(behaviourClass);
        return this;
    }

    public List<Class<? extends PipelineBehaviour<?, ?>>> getBehaviourClasses() {
        return behaviourClasses;
    }
}
