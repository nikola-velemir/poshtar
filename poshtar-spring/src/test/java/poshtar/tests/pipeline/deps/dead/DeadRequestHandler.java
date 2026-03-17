package poshtar.tests.pipeline.deps.dead;


import org.nikola.velemir.poshtar.core.annotations.RequestHandler;
import org.nikola.velemir.poshtar.core.request.handler.IRequestHandler;
import org.nikola.velemir.poshtar.core.types.Unit;

@RequestHandler
public class DeadRequestHandler implements IRequestHandler<DeadRequest, Unit> {
    @Override
    public Unit handle(DeadRequest request) {
        return Unit.Value;
    }
}
