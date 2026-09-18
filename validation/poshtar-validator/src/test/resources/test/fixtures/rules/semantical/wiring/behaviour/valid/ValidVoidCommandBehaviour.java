package test.fixtures.rules.semantical.wiring.behaviour.valid;

import io.github.nikola_velemir.poshtar.core.annotations.Behaviour;
import io.github.nikola_velemir.poshtar.core.pipeline.behaviour.PipelineBehaviour;
import io.github.nikola_velemir.poshtar.core.pipeline.delegate.RequestDelegate;
import io.github.nikola_velemir.poshtar.core.types.Unit;

@Behaviour
public class ValidVoidCommandBehaviour implements PipelineBehaviour<ValidVoidCommand, Unit> {
    @Override
    public Unit handle(ValidVoidCommand request, RequestDelegate<ValidVoidCommand, Unit> next) {
        return null;
    }
}
