package poshtar.tests.infrastructure.deps;

import org.example.core.request.handler.IRequestHandler;
import org.example.core.types.Unit;
public class AmbiguousRequestHandler2 implements IRequestHandler<AmbiguousRequest, Unit> {
    @Override
    public Unit handle(AmbiguousRequest ambiguousRequest) {
        return Unit.Value;
    }
}
