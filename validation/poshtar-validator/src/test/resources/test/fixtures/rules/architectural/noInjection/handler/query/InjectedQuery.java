package test.fixtures.rules.architectural.noInjection.handler.query;

import io.github.nikola_velemir.poshtar.core.request.Query;
import io.github.nikola_velemir.poshtar.core.types.Unit;

public record InjectedQuery() implements Query<Unit> {
}

