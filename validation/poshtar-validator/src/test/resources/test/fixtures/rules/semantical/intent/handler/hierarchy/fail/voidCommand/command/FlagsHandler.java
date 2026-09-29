package test.fixtures.rules.semantical.intent.handler.hierarchy.fail.voidCommand.command;

import io.github.nikola_velemir.poshtar.core.annotations.Handler;
import io.github.nikola_velemir.poshtar.core.request.handler.VoidCommandHandler;
import io.github.nikola_velemir.poshtar.core.types.Unit;

@Handler
public class FlagsHandler implements VoidCommandHandler<FlagsCommand> {
    @Override
    public Unit handle(FlagsCommand flagsCommand) {
        return null;
    }
}
