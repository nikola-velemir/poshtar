package io.github.nikola_velemir.poshtar.guice.adapter.pipeline.deps.dead;


import io.github.nikola_velemir.poshtar.core.annotations.Handler;
import io.github.nikola_velemir.poshtar.core.request.handler.RequestHandler;
import io.github.nikola_velemir.poshtar.core.types.Unit;

@Handler
public class DeadRequestHandler implements RequestHandler<DeadRequest, Unit> {
    @Override
    public Unit handle(DeadRequest request) {
        return Unit.Value;
    }
}
