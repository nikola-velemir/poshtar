package test.fixtures.rules.architectural.noInjection.behaviour;

import io.github.nikola_velemir.poshtar.core.request.Query;
import io.github.nikola_velemir.poshtar.core.request.Request;
import io.github.nikola_velemir.poshtar.core.types.Unit;

public record InjectedRequest() implements Query<Unit> {
}
