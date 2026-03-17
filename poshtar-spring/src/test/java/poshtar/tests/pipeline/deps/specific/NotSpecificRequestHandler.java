package poshtar.tests.pipeline.deps.specific;


import org.nikola.velemir.poshtar.core.annotations.RequestHandler;
import org.nikola.velemir.poshtar.core.request.handler.IRequestHandler;
import org.nikola.velemir.poshtar.core.types.Unit;

@RequestHandler
public class NotSpecificRequestHandler implements IRequestHandler<NotSpecificRequest, Unit> {
    @Override
    public Unit handle(NotSpecificRequest request) {
        return Unit.Value;
    }
}
