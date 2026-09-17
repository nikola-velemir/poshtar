package fixtures.rules.architectural.registration.ambiguity;

import io.github.nikola_velemir.poshtar.core.request.Request;
import io.github.nikola_velemir.poshtar.core.types.Unit;

public record AmbiguousRequest(String id) implements Request<Unit> {}