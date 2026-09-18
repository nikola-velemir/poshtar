package test.fixtures.rules.architectural.registration.ambiguity.query;

import io.github.nikola_velemir.poshtar.core.annotations.Handler;
import io.github.nikola_velemir.poshtar.core.request.handler.RequestHandler;
import io.github.nikola_velemir.poshtar.core.types.Unit;

@Handler
public class QueryHandlerAlt implements RequestHandler<AmbiguousQuery, Unit> {
    @Override
    public Unit handle(AmbiguousQuery request) {
        return null;
    }
}
