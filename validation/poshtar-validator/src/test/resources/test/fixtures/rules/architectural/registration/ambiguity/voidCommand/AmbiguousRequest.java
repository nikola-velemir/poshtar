package test.fixtures.rules.architectural.registration.ambiguity.voidCommand;

import io.github.nikola_velemir.poshtar.core.request.VoidCommand;

public record AmbiguousRequest(String id) implements VoidCommand {}