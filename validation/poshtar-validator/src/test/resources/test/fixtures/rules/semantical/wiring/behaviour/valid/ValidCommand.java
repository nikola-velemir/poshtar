package test.fixtures.rules.semantical.wiring.behaviour.valid;

import io.github.nikola_velemir.poshtar.core.request.Command;
import io.github.nikola_velemir.poshtar.core.types.Unit;

public record ValidCommand() implements Command<Unit> {}
