package test.fixtures.rules.architectural.registration.orphan.query.success;

import io.github.nikola_velemir.poshtar.core.annotations.Handler;
import io.github.nikola_velemir.poshtar.core.request.handler.QueryHandler;
import io.github.nikola_velemir.poshtar.core.types.Unit;

@Handler
public class MatchedQueryHandler implements QueryHandler<MatchedQuery, Unit> {
    @Override
    public Unit handle(MatchedQuery matchedQuery) {
        return null;
    }
}
