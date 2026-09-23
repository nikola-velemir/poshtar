package test.fixtures.rules.architectural.registration.ambiguity.command;

import io.github.nikola_velemir.poshtar.core.request.Command;
import io.github.nikola_velemir.poshtar.core.types.Unit;

public record AmbiguousRequest(String id) implements Command<Unit> {}