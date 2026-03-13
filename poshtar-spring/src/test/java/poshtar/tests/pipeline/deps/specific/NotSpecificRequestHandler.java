package poshtar.tests.pipeline.deps.specific;

import org.example.core.annotations.RequestHandler;
import org.example.core.request.handler.IRequestHandler;
import org.example.core.types.Unit;

@RequestHandler
public class NotSpecificRequestHandler implements IRequestHandler<NotSpecificRequest, Unit> {
    @Override
    public Unit handle(NotSpecificRequest request) {
        return Unit.Value;
    }
}
