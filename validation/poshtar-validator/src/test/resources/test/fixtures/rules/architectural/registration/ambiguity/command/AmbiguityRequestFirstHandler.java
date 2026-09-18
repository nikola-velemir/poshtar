package test.fixtures.rules.architectural.registration.ambiguity.command;

import io.github.nikola_velemir.poshtar.core.annotations.Handler;
import io.github.nikola_velemir.poshtar.core.request.handler.CommandHandler;
import io.github.nikola_velemir.poshtar.core.types.Unit;

@Handler
public class AmbiguityRequestFirstHandler implements CommandHandler<AmbiguousRequest,Unit> {
    @Override
    public Unit handle(AmbiguousRequest request) {
        return null;
    }
}