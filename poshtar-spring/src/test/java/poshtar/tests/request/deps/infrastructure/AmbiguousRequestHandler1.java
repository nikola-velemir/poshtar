package poshtar.tests.request.deps.infrastructure;

import org.example.core.annotations.RequestHandler;
import org.example.core.request.handler.IRequestHandler;
import org.example.core.types.Unit;

@RequestHandler
public class AmbiguousRequestHandler1 implements IRequestHandler<AmbiguousRequest, Unit> {
    @Override
    public Unit handle(AmbiguousRequest ambiguousRequest) {
        return Unit.Value;
    }
}
