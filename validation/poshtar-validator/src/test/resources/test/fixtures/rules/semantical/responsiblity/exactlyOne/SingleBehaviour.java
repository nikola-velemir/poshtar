package test.fixtures.rules.semantical.responsiblity;

import io.github.nikola_velemir.poshtar.core.annotations.Behaviour;
import io.github.nikola_velemir.poshtar.core.pipeline.behaviour.PipelineBehaviour;
import io.github.nikola_velemir.poshtar.core.pipeline.delegate.RequestDelegate;
import io.github.nikola_velemir.poshtar.core.types.Unit;

@Behaviour
public class SingleBehaviour implements PipelineBehaviour<SingleRequest, Unit> {
    @Override
    public Unit handle(SingleRequest request, RequestDelegate<SingleRequest, Unit> next) {
        return next.handle(request);
    }
}
