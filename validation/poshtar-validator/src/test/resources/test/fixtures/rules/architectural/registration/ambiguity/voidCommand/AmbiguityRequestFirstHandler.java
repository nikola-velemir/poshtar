package test.fixtures.rules.architectural.registration.ambiguity.voidCommand;

import io.github.nikola_velemir.poshtar.core.annotations.Handler;
import io.github.nikola_velemir.poshtar.core.request.handler.VoidCommandHandler;
import io.github.nikola_velemir.poshtar.core.types.Unit;

@Handler
public class AmbiguityRequestFirstHandler implements VoidCommandHandler<AmbiguousRequest> {
    @Override
    public Unit handle(AmbiguousRequest request) {
        return null;
    }
}