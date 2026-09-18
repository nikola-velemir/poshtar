package test.fixtures.rules.architectural.registration.orphan.query.fail;

import io.github.nikola_velemir.poshtar.core.request.Query;
import io.github.nikola_velemir.poshtar.core.types.Unit;

public record UnhandledQuery() implements Query<Unit> {
}
