package io.github.nikola.velemir.poshtar.quarkus.it.component.request.deps.nullRequest;

import io.github.nikola_velemir.poshtar.core.request.Request;
import io.github.nikola_velemir.poshtar.core.types.Unit;
import io.github.nikola_velemir.poshtar.validator.api.annotations.request.SuppressOrphan;

@SuppressOrphan
public final class NullRequest implements Request<Unit> {
}
