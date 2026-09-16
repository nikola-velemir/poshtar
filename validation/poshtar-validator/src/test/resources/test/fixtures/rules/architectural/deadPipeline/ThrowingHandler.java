package test.fixtures.rules.architectural.deadPipeline;

import io.github.nikola_velemir.poshtar.core.annotations.Handler;
import io.github.nikola_velemir.poshtar.core.request.handler.RequestHandler;
import io.github.nikola_velemir.poshtar.core.types.Unit;

@Handler
public class ThrowingHandler implements RequestHandler<ThrowingRequest, Unit> {
    @Override
    public Unit handle(ThrowingRequest request) {
        return Unit.Value;
    }
}
