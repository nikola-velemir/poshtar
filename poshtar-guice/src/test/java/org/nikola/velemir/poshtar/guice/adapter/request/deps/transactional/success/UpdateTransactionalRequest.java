package org.nikola.velemir.poshtar.guice.adapter.request.deps.transactional.success;

import org.nikola.velemir.poshtar.core.request.Request;
import org.nikola.velemir.poshtar.core.types.Unit;

public record UpdateTransactionalRequest(Long id, String data) implements Request<Unit> {
}
