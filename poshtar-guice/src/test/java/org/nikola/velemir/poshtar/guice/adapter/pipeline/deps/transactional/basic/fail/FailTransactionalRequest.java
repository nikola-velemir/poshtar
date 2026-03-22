package org.nikola.velemir.poshtar.guice.adapter.pipeline.deps.transactional.basic.fail;

import org.nikola.velemir.poshtar.core.request.Request;
import org.nikola.velemir.poshtar.core.types.Unit;

public record FailTransactionalRequest(String payload) implements Request<Unit> {
}
