package poshtar.tests.request.deps.infrastructure;


import org.nikola.velemir.poshtar.core.annotations.RequestHandler;
import org.nikola.velemir.poshtar.core.request.handler.IRequestHandler;
import org.nikola.velemir.poshtar.core.types.Unit;

@RequestHandler
public class AmbiguousRequestHandler1 implements IRequestHandler<AmbiguousRequest, Unit> {
    @Override
    public Unit handle(AmbiguousRequest ambiguousRequest) {
        return Unit.Value;
    }
}
