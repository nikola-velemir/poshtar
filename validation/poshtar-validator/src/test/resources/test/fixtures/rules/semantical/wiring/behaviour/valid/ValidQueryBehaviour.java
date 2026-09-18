package test.fixtures.rules.semantical.wiring.behaviour.valid;

import io.github.nikola_velemir.poshtar.core.annotations.Behaviour;
import io.github.nikola_velemir.poshtar.core.pipeline.behaviour.PipelineBehaviour;
import io.github.nikola_velemir.poshtar.core.pipeline.delegate.RequestDelegate;
import io.github.nikola_velemir.poshtar.core.types.Unit;

@Behaviour
public class ValidQueryBehaviour implements PipelineBehaviour<ValidQuery, Unit> {
    @Override
    public Unit handle(ValidQuery request, RequestDelegate<ValidQuery, Unit> next) {
        return null;
    }
}
