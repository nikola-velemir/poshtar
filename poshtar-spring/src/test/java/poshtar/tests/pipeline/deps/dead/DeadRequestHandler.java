package poshtar.tests.pipeline.deps.dead;

import org.example.core.annotations.RequestHandler;
import org.example.core.request.handler.IRequestHandler;
import org.example.core.types.Unit;

@RequestHandler
public class DeadRequestHandler implements IRequestHandler<DeadRequest, Unit> {
    @Override
    public Unit handle(DeadRequest request) {
        return Unit.Value;
    }
}
