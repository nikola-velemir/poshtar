package test.fixtures.rules.architectural.deadPipeline;

import io.github.nikola_velemir.poshtar.core.annotations.Handler;
import io.github.nikola_velemir.poshtar.core.request.handler.RequestHandler;
import io.github.nikola_velemir.poshtar.core.types.Unit;

@Handler
public class DeadPipelineRequestHandler implements RequestHandler<DeadPipelineRequest, Unit> {
    @Override
    public Unit handle(DeadPipelineRequest request) {
        return null;
    }
}
