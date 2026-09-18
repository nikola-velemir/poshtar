package test.fixtures.rules.architectural.registration.orphan.query.success;

import io.github.nikola_velemir.poshtar.core.request.Query;
import io.github.nikola_velemir.poshtar.core.types.Unit;
import io.github.nikola_velemir.poshtar.validator.api.annotations.request.SuppressOrphan;

@SuppressOrphan
public record SuppressedOrphanQuery() implements Query<Unit> {
}
