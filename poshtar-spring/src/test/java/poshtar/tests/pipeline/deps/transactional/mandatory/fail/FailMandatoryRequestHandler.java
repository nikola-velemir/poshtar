package poshtar.tests.pipeline.deps.transactional.mandatory.fail;

import org.nikola.velemir.poshtar.core.annotations.Handler;
import org.nikola.velemir.poshtar.core.request.handler.RequestHandler;
import org.nikola.velemir.poshtar.core.types.Unit;

@Handler
public class FailMandatoryRequestHandler implements RequestHandler<FailMandatoryRequest, Unit> {
    @Override
    public Unit handle(FailMandatoryRequest request) {
        request.payload += 1;
        return Unit.Value;
    }
}
