package test.fixtures.rules.semantical.wiring.handler.fail.query;


import io.github.nikola_velemir.poshtar.core.annotations.Handler;
import io.github.nikola_velemir.poshtar.core.pipeline.behaviour.PipelineBehaviour;
import io.github.nikola_velemir.poshtar.core.pipeline.delegate.RequestDelegate;
import io.github.nikola_velemir.poshtar.core.types.Unit;

@Handler
public class FailWithBehaviourHandler implements PipelineBehaviour<FailWithBehaviourQuery, Unit> {
    @Override
    public Unit handle(FailWithBehaviourQuery failWithBehaviourRequest, RequestDelegate<FailWithBehaviourQuery, Unit> requestDelegate) {
        return null;
    }
}