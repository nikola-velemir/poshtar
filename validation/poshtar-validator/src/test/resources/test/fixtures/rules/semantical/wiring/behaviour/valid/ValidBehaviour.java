package test.fixtures.rules.semantical.wiring.valid;

import io.github.nikola_velemir.poshtar.core.annotations.Behaviour;
import io.github.nikola_velemir.poshtar.core.pipeline.behaviour.PipelineBehaviour;
import io.github.nikola_velemir.poshtar.core.pipeline.delegate.RequestDelegate;
import io.github.nikola_velemir.poshtar.core.types.Unit;

@Behaviour
public class ValidBehaviour implements PipelineBehaviour<ValidRequest, Unit> {
    @Override
    public Unit handle(ValidRequest validRequest, RequestDelegate<ValidRequest, Unit> requestDelegate) {
        return null;
    }
}
