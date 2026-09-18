package test.fixtures.rules.architectural.noInjection.handler.command;

import io.github.nikola_velemir.poshtar.core.request.Command;
import io.github.nikola_velemir.poshtar.core.types.Unit;

public record InjectedCommand() implements Command<Unit> {
}

