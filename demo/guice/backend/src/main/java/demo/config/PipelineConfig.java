package demo.config;

import demo.pipeline.ActivationBehaviour;
import demo.pipeline.GetBehaviour;
import demo.pipeline.LoggingBehaviour;
import demo.pipeline.RegisterBehaviour;
import org.nikola.velemir.poshtar.adapter.configuration.PipelineConfigurer;

public class PipelineConfig {
    static PipelineConfigurer providePipelineConfigurer(){
        return new PipelineConfigurer()
                .add(RegisterBehaviour.class)
                .add(LoggingBehaviour.class)
                .add(GetBehaviour.class)
                .add(ActivationBehaviour.class);
    }
}
