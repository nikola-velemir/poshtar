package test.fixtures.rules.semantical.wiring.behaviour.fail.queryHandler;

import io.github.nikola_velemir.poshtar.core.annotations.Behaviour;
import io.github.nikola_velemir.poshtar.core.request.handler.QueryHandler;
import io.github.nikola_velemir.poshtar.core.types.Unit;

@Behaviour
public class FailQueryHandler implements QueryHandler<FailQuery, Unit> {
    @Override
    public Unit handle(FailQuery request) {
        return null;
    }
}
