package poshtar.tests.request.deps.infrastructure;

import org.nikola.velemir.poshtar.core.request.handler.RequestHandler;
import org.nikola.velemir.poshtar.core.types.Unit;

public class AmbiguousRequestHandler2 implements RequestHandler<AmbiguousRequest, Unit> {
    @Override
    public Unit handle(AmbiguousRequest ambiguousRequest) {
        return Unit.Value;
    }
}
