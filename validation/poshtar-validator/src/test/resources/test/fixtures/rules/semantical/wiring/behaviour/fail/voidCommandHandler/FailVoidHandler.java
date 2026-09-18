package test.fixtures.rules.semantical.wiring.behaviour.fail.voidCommandHandler;

import io.github.nikola_velemir.poshtar.core.annotations.Behaviour;
import io.github.nikola_velemir.poshtar.core.request.handler.VoidCommandHandler;
import io.github.nikola_velemir.poshtar.core.types.Unit;

@Behaviour
public class FailVoidHandler implements VoidCommandHandler<FailVoidCommand> {
    @Override
    public Unit handle(FailVoidCommand request) {
        return null;
    }
}
