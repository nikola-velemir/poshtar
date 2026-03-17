package poshtar.tests.pipeline.deps.transactional.mandatory.fail;


import org.nikola.velemir.poshtar.core.annotations.RequestHandler;
import org.nikola.velemir.poshtar.core.request.handler.IRequestHandler;
import org.nikola.velemir.poshtar.core.types.Unit;

@RequestHandler
public class FailMandatoryRequestHandler implements IRequestHandler<FailMandatoryRequest, Unit> {
    @Override
    public Unit handle(FailMandatoryRequest request) {
        request.payload += 1;
        return Unit.Value;
    }
}
