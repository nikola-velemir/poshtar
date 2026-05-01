package io.github.nikola_velemir.poshtar.spring.adapter.pipeline.deps.specific;

import io.github.nikola_velemir.poshtar.core.annotations.Handler;
import io.github.nikola_velemir.poshtar.core.request.handler.RequestHandler;
import io.github.nikola_velemir.poshtar.core.types.Unit;

@Handler
public class SpecificRequestHandler implements RequestHandler<SpecificRequest, Unit> {
    @Override
    public Unit handle(SpecificRequest request) {
        return Unit.Value;
    }
}
