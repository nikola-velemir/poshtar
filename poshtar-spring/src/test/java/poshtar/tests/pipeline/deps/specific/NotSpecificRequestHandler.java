package poshtar.tests.pipeline.deps.specific;

import org.nikola.velemir.poshtar.core.annotations.Handler;
import org.nikola.velemir.poshtar.core.request.handler.RequestHandler;
import org.nikola.velemir.poshtar.core.types.Unit;

@Handler
public class NotSpecificRequestHandler implements RequestHandler<NotSpecificRequest, Unit> {
    @Override
    public Unit handle(NotSpecificRequest request) {
        return Unit.Value;
    }
}
