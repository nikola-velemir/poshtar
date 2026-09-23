package test.fixtures.rules.semantical.wiring.behaviour.fail.commandHandler;

import io.github.nikola_velemir.poshtar.core.annotations.Behaviour;
import io.github.nikola_velemir.poshtar.core.request.handler.CommandHandler;
import io.github.nikola_velemir.poshtar.core.types.Unit;

@Behaviour
public class FailBehaviour implements CommandHandler<FailCommand, Unit> {
    @Override
    public Unit handle(FailCommand request) {
        return null;
    }
}
