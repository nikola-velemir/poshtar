package test.fixtures.rules.architectural.deadPipeline;

import io.github.nikola_velemir.poshtar.core.annotations.Behaviour;
import io.github.nikola_velemir.poshtar.core.pipeline.behaviour.PipelineBehaviour;
import io.github.nikola_velemir.poshtar.core.pipeline.delegate.RequestDelegate;
import io.github.nikola_velemir.poshtar.core.types.Unit;

@Behaviour
public class AlivePipelineBehaviour implements PipelineBehaviour<AlivePipelineRequest, Unit> {
    @Override
    public Unit handle(AlivePipelineRequest request, RequestDelegate<AlivePipelineRequest, Unit> next) {
        return next.handle(request);
    }
}

