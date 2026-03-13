package poshtar.tests.pipeline.deps.global;

import org.example.core.annotations.RequestHandler;
import org.example.core.request.handler.IRequestHandler;
import org.example.core.types.Unit;

@RequestHandler
public class GlobalPipelineTestRequestHandler implements IRequestHandler<GlobalPipelineTestRequest, Unit> {
    @Override
    public Unit handle(GlobalPipelineTestRequest request) {
        return Unit.Value;
    }
}
