package test.fixtures.rules.architectural.deadPipeline.shadow.dead;

import io.github.nikola_velemir.poshtar.core.annotations.Handler;
import io.github.nikola_velemir.poshtar.core.request.handler.RequestHandler;
import io.github.nikola_velemir.poshtar.core.types.Unit;

@Handler
public class ShadowDeadHandler implements RequestHandler<ShadowDeadRequest, Unit> {
    @Override
    public Unit handle(ShadowDeadRequest request) {
        return Unit.Value;
    }
}
