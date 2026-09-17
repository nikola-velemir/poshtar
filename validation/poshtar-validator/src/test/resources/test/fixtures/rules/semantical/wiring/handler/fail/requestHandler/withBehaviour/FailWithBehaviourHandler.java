package test.fixtures.rules.semantical.wiring.handler.fail.requestHandler.withBehaviour;

import io.github.nikola_velemir.poshtar.core.annotations.Handler;
import io.github.nikola_velemir.poshtar.core.pipeline.behaviour.PipelineBehaviour;
import io.github.nikola_velemir.poshtar.core.pipeline.delegate.RequestDelegate;
import io.github.nikola_velemir.poshtar.core.types.Unit;

@Handler
public class FailWithBehaviourHandler implements PipelineBehaviour<FailWithBehaviourRequest, Unit> {
    @Override
    public Unit handle(FailWithBehaviourRequest failWithBehaviourRequest, RequestDelegate<FailWithBehaviourRequest, Unit> requestDelegate) {
        return null;
    }
}
