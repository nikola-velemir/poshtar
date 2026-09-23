package fixtures.rules.architectural.finality.request;

import io.github.nikola_velemir.poshtar.core.annotations.Handler;
import io.github.nikola_velemir.poshtar.core.request.handler.CommandHandler;
import io.github.nikola_velemir.poshtar.core.request.handler.RequestHandler;
import io.github.nikola_velemir.poshtar.core.types.Unit;
@Handler
public class FinalRequestHandler implements CommandHandler<FinalRequest, Unit> {
    @Override
    public Unit handle(FinalRequest request) {
        return null;
    }
}
