package fixtures.rules.architectural.registration.ambiguity;

import io.github.nikola_velemir.poshtar.core.annotations.Handler;
import io.github.nikola_velemir.poshtar.core.request.handler.RequestHandler;
import io.github.nikola_velemir.poshtar.core.types.Unit;

@Handler
public class AmbiguityRequestFirstHandler implements RequestHandler<AmbiguousRequest,Unit> {
    @Override
    public Unit handle(AmbiguousRequest request) {
        return null;
    }
}