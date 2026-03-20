package org.nikola.velemir.poshtar.guice.adapter.pipeline.deps.dead;


import org.nikola.velemir.poshtar.core.annotations.Handler;
import org.nikola.velemir.poshtar.core.request.handler.RequestHandler;
import org.nikola.velemir.poshtar.core.types.Unit;

@Handler
public class DeadRequestHandler implements RequestHandler<DeadRequest, Unit> {
    @Override
    public Unit handle(DeadRequest request) {
        return Unit.Value;
    }
}
