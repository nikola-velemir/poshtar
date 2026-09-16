package test.fixtures.rules.architectural.deadPipeline;

import io.github.nikola_velemir.poshtar.core.annotations.Behaviour;
import io.github.nikola_velemir.poshtar.core.pipeline.behaviour.PipelineBehaviour;
import io.github.nikola_velemir.poshtar.core.pipeline.delegate.RequestDelegate;
import io.github.nikola_velemir.poshtar.core.types.Unit;

@Behaviour
public class DoubleThrowingBehaviour implements PipelineBehaviour<ThrowingRequest, Unit> {
    @Override
    public Unit handle(ThrowingRequest request, RequestDelegate<ThrowingRequest, Unit> next) {
        if (true)
            throw new RuntimeException("A");
        throw new IllegalArgumentException("B");
    }
}
