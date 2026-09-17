package test.fixtures.rules.architectural.noInjection.behaviour;

import io.github.nikola_velemir.poshtar.core.annotations.Behaviour;
import io.github.nikola_velemir.poshtar.core.pipeline.behaviour.PipelineBehaviour;
import io.github.nikola_velemir.poshtar.core.pipeline.delegate.RequestDelegate;
import io.github.nikola_velemir.poshtar.core.types.Unit;

@Behaviour
public class InjectedRequestBehaviour implements PipelineBehaviour<InjectedRequest, Unit> {
    @Override
    public Unit handle(InjectedRequest request, RequestDelegate<InjectedRequest, Unit> next) {
        return next.handle(request);
    }
}
