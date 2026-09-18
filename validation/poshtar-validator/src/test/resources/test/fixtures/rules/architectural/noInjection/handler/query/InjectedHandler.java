package test.fixtures.rules.architectural.noInjection.handler.query;

import io.github.nikola_velemir.poshtar.core.annotations.Handler;
import io.github.nikola_velemir.poshtar.core.request.handler.QueryHandler;
import io.github.nikola_velemir.poshtar.core.types.Unit;

@Handler
public class InjectedHandler implements QueryHandler<InjectedQuery, Unit> {
    @Override
    public Unit handle(InjectedQuery injectedQuery) {
        return null;
    }
}
