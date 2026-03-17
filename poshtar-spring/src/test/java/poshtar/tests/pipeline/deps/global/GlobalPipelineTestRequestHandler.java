package poshtar.tests.pipeline.deps.global;

import org.nikola.velemir.poshtar.core.annotations.Handler;
import org.nikola.velemir.poshtar.core.request.handler.RequestHandler;
import org.nikola.velemir.poshtar.core.types.Unit;

@Handler
public class GlobalPipelineTestRequestHandler implements RequestHandler<GlobalPipelineTestRequest, Unit> {
    @Override
    public Unit handle(GlobalPipelineTestRequest request) {
        return Unit.Value;
    }
}
