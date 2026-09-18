package test.fixtures.rules.architectural.registration.ambiguity.query;

import io.github.nikola_velemir.poshtar.core.annotations.Handler;
import io.github.nikola_velemir.poshtar.core.types.Unit;

@Handler
public class QueryHandler implements io.github.nikola_velemir.poshtar.core.request.handler.QueryHandler<AmbiguousQuery, Unit> {
    @Override
    public Unit handle(AmbiguousQuery request) {
        return null;
    }
}
