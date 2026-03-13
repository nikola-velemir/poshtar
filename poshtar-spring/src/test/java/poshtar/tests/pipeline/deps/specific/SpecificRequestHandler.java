package poshtar.tests.pipeline.deps.specific;

import org.example.core.annotations.RequestHandler;
import org.example.core.request.handler.IRequestHandler;
import org.example.core.types.Unit;

@RequestHandler
public class SpecificRequestHandler implements IRequestHandler<SpecificRequest, Unit> {
    @Override
    public Unit handle(SpecificRequest request) {
        return Unit.Value;
    }
}
