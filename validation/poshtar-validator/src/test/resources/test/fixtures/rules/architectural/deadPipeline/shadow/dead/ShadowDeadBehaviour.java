package test.fixtures.rules.architectural.deadPipeline.shadow.dead;

import io.github.nikola_velemir.poshtar.core.annotations.Behaviour;
import io.github.nikola_velemir.poshtar.core.pipeline.behaviour.PipelineBehaviour;
import io.github.nikola_velemir.poshtar.core.pipeline.delegate.RequestDelegate;
import io.github.nikola_velemir.poshtar.core.types.Unit;

@Behaviour
public class ShadowDeadBehaviour implements PipelineBehaviour<ShadowDeadRequest, Unit> {

    private Unit calculateReturnType(RequestDelegate<ShadowDeadRequest, Unit> delegate) {
        return null;
    }

    @Override
    public Unit handle(ShadowDeadRequest request, RequestDelegate<ShadowDeadRequest, Unit> next) {
        return calculateReturnType(next);
    }
}
