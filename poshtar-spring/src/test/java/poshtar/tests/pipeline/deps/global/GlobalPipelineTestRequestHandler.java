package poshtar.tests.pipeline.deps.global;


import org.nikola.velemir.poshtar.core.annotations.RequestHandler;
import org.nikola.velemir.poshtar.core.request.handler.IRequestHandler;
import org.nikola.velemir.poshtar.core.types.Unit;

@RequestHandler
public class GlobalPipelineTestRequestHandler implements IRequestHandler<GlobalPipelineTestRequest, Unit> {
    @Override
    public Unit handle(GlobalPipelineTestRequest request) {
        return Unit.Value;
    }
}
