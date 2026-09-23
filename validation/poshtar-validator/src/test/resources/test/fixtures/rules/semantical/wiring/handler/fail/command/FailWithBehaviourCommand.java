package test.fixtures.rules.semantical.wiring.handler.fail.command;

import io.github.nikola_velemir.poshtar.core.request.Command;
import io.github.nikola_velemir.poshtar.core.types.Unit;

public record FailWithBehaviourCommand() implements Command<Unit> {
}

