package fixtures.rules.architectural.finality.request;

import io.github.nikola_velemir.poshtar.core.annotations.Handler;
import io.github.nikola_velemir.poshtar.core.request.handler.RequestHandler;
import io.github.nikola_velemir.poshtar.core.types.Unit;
@Handler
public class NonFinalRequestHandler implements RequestHandler<NonFinalRequest, Unit> {
    @Override
    public Unit handle(NonFinalRequest request) {
        return null;
    }
}
