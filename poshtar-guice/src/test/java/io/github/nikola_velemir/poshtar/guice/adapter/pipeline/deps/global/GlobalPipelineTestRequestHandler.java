package io.github.nikola_velemir.poshtar.guice.adapter.pipeline.deps.global;


import io.github.nikola_velemir.poshtar.core.annotations.Handler;
import io.github.nikola_velemir.poshtar.core.request.handler.RequestHandler;
import io.github.nikola_velemir.poshtar.core.types.Unit;

@Handler
public class GlobalPipelineTestRequestHandler implements RequestHandler<GlobalPipelineTestRequest, Unit> {
    @Override
    public Unit handle(GlobalPipelineTestRequest request) {
        return Unit.Value;
    }
}
