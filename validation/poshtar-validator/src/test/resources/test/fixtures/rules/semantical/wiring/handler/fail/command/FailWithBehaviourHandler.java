package test.fixtures.rules.semantical.wiring.handler.fail.command;

import io.github.nikola_velemir.poshtar.core.annotations.Handler;
import io.github.nikola_velemir.poshtar.core.pipeline.behaviour.PipelineBehaviour;
import io.github.nikola_velemir.poshtar.core.pipeline.delegate.RequestDelegate;
import io.github.nikola_velemir.poshtar.core.types.Unit;

@Handler
public class FailWithBehaviourHandler implements PipelineBehaviour<FailWithBehaviourCommand, Unit> {
    @Override
    public Unit handle(FailWithBehaviourCommand request, RequestDelegate<FailWithBehaviourCommand, Unit> next) {
        return null;
    }
}
