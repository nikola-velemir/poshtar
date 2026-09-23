package test.fixtures.rules.architectural.noInjection.behaviour;

import io.github.nikola_velemir.poshtar.core.annotations.Handler;
import io.github.nikola_velemir.poshtar.core.request.handler.QueryHandler;
import io.github.nikola_velemir.poshtar.core.request.handler.RequestHandler;
import io.github.nikola_velemir.poshtar.core.types.Unit;

@Handler
public class InjectedRequestHandler implements QueryHandler<InjectedRequest, Unit> {
    @Override
    public Unit handle(InjectedRequest request) {
        return null;
    }
}
