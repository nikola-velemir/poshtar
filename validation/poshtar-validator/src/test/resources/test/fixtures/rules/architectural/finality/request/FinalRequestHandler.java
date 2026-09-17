package fixtures.rules.architectural.finality.request;

import io.github.nikola_velemir.poshtar.core.annotations.Handler;
import io.github.nikola_velemir.poshtar.core.request.handler.RequestHandler;
import io.github.nikola_velemir.poshtar.core.types.Unit;
@Handler
public class FinalRequestHandler implements RequestHandler<FinalRequest, Unit> {
    @Override
    public Unit handle(FinalRequest request) {
        return null;
    }
}
