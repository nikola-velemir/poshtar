package io.github.nikola_velemir.poshtar.guice.adapter.request.deps.transactional.success;

import io.github.nikola_velemir.poshtar.core.request.Request;
import io.github.nikola_velemir.poshtar.core.types.Unit;

public record UpdateTransactionalRequest(Long id, String data) implements Request<Unit> {
}
