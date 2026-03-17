package poshtar.tests.pipeline.deps.specific;


import org.nikola.velemir.poshtar.core.annotations.RequestHandler;
import org.nikola.velemir.poshtar.core.request.handler.IRequestHandler;
import org.nikola.velemir.poshtar.core.types.Unit;

@RequestHandler
public class SpecificRequestHandler implements IRequestHandler<SpecificRequest, Unit> {
    @Override
    public Unit handle(SpecificRequest request) {
        return Unit.Value;
    }
}
