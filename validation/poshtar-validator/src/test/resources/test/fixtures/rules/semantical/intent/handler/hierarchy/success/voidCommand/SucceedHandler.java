package test.fixtures.rules.semantical.intent.handler.hierarchy.success.voidCommand;

import io.github.nikola_velemir.poshtar.core.annotations.Handler;
import io.github.nikola_velemir.poshtar.core.request.handler.VoidCommandHandler;
import io.github.nikola_velemir.poshtar.core.types.Unit;

@Handler
public class SucceedHandler implements VoidCommandHandler<SucceedVoidCommand> {
    @Override
    public Unit handle(SucceedVoidCommand succeedVoidCommand) {
        return null;
    }
}
