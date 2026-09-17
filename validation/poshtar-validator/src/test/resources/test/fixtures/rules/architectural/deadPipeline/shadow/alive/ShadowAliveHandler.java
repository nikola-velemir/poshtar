package io.github.nikola_velemir.poshtar.validator.rules.architectural.deadPipeline.shadow.alive;

import io.github.nikola_velemir.poshtar.core.annotations.Handler;
import io.github.nikola_velemir.poshtar.core.request.handler.RequestHandler;
import io.github.nikola_velemir.poshtar.core.types.Unit;

@Handler
public class ShadowAliveHandler implements RequestHandler<ShadowAliveRequest, Unit> {
    @Override
    public Unit handle(ShadowAliveRequest request) {
        return Unit.Value;
    }
}
