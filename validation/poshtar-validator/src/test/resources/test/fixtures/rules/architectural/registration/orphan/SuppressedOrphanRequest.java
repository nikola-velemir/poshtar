package test.fixtures.rules.architectural.registration.orphan;

import io.github.nikola_velemir.poshtar.core.request.Request;
import io.github.nikola_velemir.poshtar.core.types.Unit;
import io.github.nikola_velemir.poshtar.validator.api.annotations.request.SuppressOrphan;

@SuppressOrphan
public final class SuppressedOrphanRequest implements Request<Unit> {
}
