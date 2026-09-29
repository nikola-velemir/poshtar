package test.fixtures.rules.semantical.intent.handler.hierarchy.fail.command;

import io.github.nikola_velemir.poshtar.core.annotations.Handler;
import io.github.nikola_velemir.poshtar.core.request.handler.RequestHandler;
import io.github.nikola_velemir.poshtar.core.types.Unit;

@Handler
public class FlagsHandler implements RequestHandler<FlagsCommand, Unit> {
    @Override
    public Unit handle(FlagsCommand flagsCommand) {
        return null;
    }
}
