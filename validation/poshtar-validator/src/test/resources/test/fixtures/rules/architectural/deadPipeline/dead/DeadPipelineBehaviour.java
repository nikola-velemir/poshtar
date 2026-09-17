package test.fixtures.rules.architectural.deadPipeline;

import io.github.nikola_velemir.poshtar.core.annotations.Behaviour;
import io.github.nikola_velemir.poshtar.core.pipeline.behaviour.PipelineBehaviour;
import io.github.nikola_velemir.poshtar.core.pipeline.delegate.RequestDelegate;
import io.github.nikola_velemir.poshtar.core.types.Unit;

@Behaviour
public class DeadPipelineBehaviour implements PipelineBehaviour<DeadPipelineRequest, Unit> {
    @Override
    public Unit handle(DeadPipelineRequest request, RequestDelegate<DeadPipelineRequest, Unit> next) {
        return null;
    }
}
