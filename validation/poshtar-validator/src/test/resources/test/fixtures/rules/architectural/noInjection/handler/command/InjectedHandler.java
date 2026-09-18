package test.fixtures.rules.architectural.noInjection.handler.command;

import io.github.nikola_velemir.poshtar.core.annotations.Handler;
import io.github.nikola_velemir.poshtar.core.request.handler.CommandHandler;
import io.github.nikola_velemir.poshtar.core.types.Unit;

@Handler
public class InjectedHandler implements CommandHandler<InjectedCommand, Unit> {
    @Override
    public Unit handle(InjectedCommand injectedCommand) {
        return null;
    }
}
