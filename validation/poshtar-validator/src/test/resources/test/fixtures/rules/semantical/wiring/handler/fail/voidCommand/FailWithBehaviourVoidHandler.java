package test.fixtures.rules.semantical.wiring.handler.fail.voidCommand;

import io.github.nikola_velemir.poshtar.core.annotations.Handler;
import io.github.nikola_velemir.poshtar.core.pipeline.behaviour.PipelineBehaviour;
import io.github.nikola_velemir.poshtar.core.pipeline.delegate.RequestDelegate;
import io.github.nikola_velemir.poshtar.core.types.Unit;

@Handler
public class FailWithBehaviourVoidHandler implements PipelineBehaviour<FailWithBehaviourVoidCommand, Unit> {
    @Override
    public Unit handle(FailWithBehaviourVoidCommand request, RequestDelegate<FailWithBehaviourVoidCommand, Unit> next) {
        return null;
    }
}
