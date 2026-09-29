package test.fixtures.rules.semantical.intent.handler.hierarchy.success.command;

import io.github.nikola_velemir.poshtar.core.annotations.Handler;
import io.github.nikola_velemir.poshtar.core.request.handler.CommandHandler;
import io.github.nikola_velemir.poshtar.core.types.Unit;

@Handler
public class SucceedsHandler implements CommandHandler<SucceedsCommand, Unit> {
    @Override
    public Unit handle(SucceedsCommand succeedsCommand) {
        return null;
    }
}
