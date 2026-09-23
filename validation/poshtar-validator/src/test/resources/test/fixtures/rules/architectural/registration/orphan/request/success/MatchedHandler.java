package fixtures.rules.architectural.registration.orphan;

import io.github.nikola_velemir.poshtar.core.annotations.Handler;
import io.github.nikola_velemir.poshtar.core.request.handler.CommandHandler;
import io.github.nikola_velemir.poshtar.core.request.handler.RequestHandler;
import io.github.nikola_velemir.poshtar.core.types.Unit;

@Handler
public class MatchedHandler implements CommandHandler<MatchedRequest, Unit> {
    @Override
    public Unit handle(MatchedRequest request) {
        return null;
    }
}
