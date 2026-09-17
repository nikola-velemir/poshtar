package test.fixtures.rules.semantical.wiring.handler.success;

import io.github.nikola_velemir.poshtar.core.annotations.Handler;
import io.github.nikola_velemir.poshtar.core.request.handler.RequestHandler;
import io.github.nikola_velemir.poshtar.core.types.Unit;

@Handler
public class ValidHandler implements RequestHandler<ValidRequest, Unit> {
    @Override
    public Unit handle(ValidRequest validRequest) {
        return null;
    }
}
