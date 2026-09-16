package test.fixtures.rules.architectural.deadPipeline;

import io.github.nikola_velemir.poshtar.core.annotations.Behaviour;
import io.github.nikola_velemir.poshtar.core.pipeline.behaviour.PipelineBehaviour;
import io.github.nikola_velemir.poshtar.core.pipeline.delegate.RequestDelegate;
import io.github.nikola_velemir.poshtar.core.types.Unit;

@Behaviour
public class BasicThrowingBehaviour implements PipelineBehaviour<ThrowingRequest, Unit> {
    @Override
    public Unit handle(ThrowingRequest request, RequestDelegate<ThrowingRequest, Unit> next) {
        throw new RuntimeException("A");
    }
}
