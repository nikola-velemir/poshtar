package poshtar.tests.pipeline.deps.specific;

import org.nikola.velemir.poshtar.core.annotations.Handler;
import org.nikola.velemir.poshtar.core.request.handler.RequestHandler;
import org.nikola.velemir.poshtar.core.types.Unit;

@Handler
public class SpecificRequestHandler implements RequestHandler<SpecificRequest, Unit> {
    @Override
    public Unit handle(SpecificRequest request) {
        return Unit.Value;
    }
}
