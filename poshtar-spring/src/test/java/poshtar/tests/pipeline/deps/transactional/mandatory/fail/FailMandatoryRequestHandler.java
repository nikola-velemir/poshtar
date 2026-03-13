package poshtar.tests.pipeline.deps.transactional.mandatory.fail;

import org.example.core.annotations.RequestHandler;
import org.example.core.request.handler.IRequestHandler;
import org.example.core.types.Unit;

@RequestHandler
public class FailMandatoryRequestHandler implements IRequestHandler<FailMandatoryRequest, Unit> {
    @Override
    public Unit handle(FailMandatoryRequest request) {
        request.payload += 1;
        return Unit.Value;
    }
}
