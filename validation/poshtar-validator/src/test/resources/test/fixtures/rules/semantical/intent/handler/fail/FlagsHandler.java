package test.fixtures.rules.semantical.intent.handler;

import io.github.nikola_velemir.poshtar.core.annotations.Handler;
import io.github.nikola_velemir.poshtar.core.request.handler.RequestHandler;
import io.github.nikola_velemir.poshtar.core.types.Unit;

@Handler
public class FlagsHandler implements RequestHandler<FlagsRequest, Unit> {
    @Override
    public Unit handle(FlagsRequest flagsRequest) {
        return null;
    }
}
