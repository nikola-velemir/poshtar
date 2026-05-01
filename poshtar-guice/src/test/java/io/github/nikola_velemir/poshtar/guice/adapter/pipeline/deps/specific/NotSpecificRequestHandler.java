package io.github.nikola_velemir.poshtar.guice.adapter.pipeline.deps.specific;

import io.github.nikola_velemir.poshtar.core.annotations.Handler;
import io.github.nikola_velemir.poshtar.core.request.handler.RequestHandler;
import io.github.nikola_velemir.poshtar.core.types.Unit;

@Handler
public class NotSpecificRequestHandler implements RequestHandler<NotSpecificRequest, Unit> {
    @Override
    public Unit handle(NotSpecificRequest request) {
        return Unit.Value;
    }
}
