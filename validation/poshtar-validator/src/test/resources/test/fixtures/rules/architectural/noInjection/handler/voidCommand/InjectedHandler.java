package test.fixtures.rules.architectural.noInjection.handler.voidCommand;

import io.github.nikola_velemir.poshtar.core.annotations.Handler;
import io.github.nikola_velemir.poshtar.core.request.handler.VoidCommandHandler;
import io.github.nikola_velemir.poshtar.core.types.Unit;

@Handler
public class InjectedHandler implements VoidCommandHandler<InjectedVoidCommand> {
    @Override
    public Unit handle(InjectedVoidCommand injectedVoidCommand) {
        return null;
    }
}
