package test.fixtures.rules.semantical.wiring.behaviour.fail.commandHandler;

import io.github.nikola_velemir.poshtar.core.request.Command;
import io.github.nikola_velemir.poshtar.core.types.Unit;

public record FailCommand() implements Command<Unit> {
}

