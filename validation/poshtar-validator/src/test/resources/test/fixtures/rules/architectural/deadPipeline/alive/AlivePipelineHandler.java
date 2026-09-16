package test.fixtures.rules.architectural.deadPipeline;

import io.github.nikola_velemir.poshtar.core.annotations.Handler;
import io.github.nikola_velemir.poshtar.core.request.handler.RequestHandler;
import io.github.nikola_velemir.poshtar.core.types.Unit;

@Handler
public class AlivePipelineHandler implements RequestHandler<AlivePipelineRequest, Unit> {
    @Override
    public Unit handle(AlivePipelineRequest request) {
        return null;
    }
}
