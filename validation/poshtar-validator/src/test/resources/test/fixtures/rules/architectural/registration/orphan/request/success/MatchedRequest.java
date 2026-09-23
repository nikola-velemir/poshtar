package fixtures.rules.architectural.registration.orphan;

import io.github.nikola_velemir.poshtar.core.request.Command;
import io.github.nikola_velemir.poshtar.core.request.Request;
import io.github.nikola_velemir.poshtar.core.types.Unit;

public record MatchedRequest() implements Command<Unit> {
}
