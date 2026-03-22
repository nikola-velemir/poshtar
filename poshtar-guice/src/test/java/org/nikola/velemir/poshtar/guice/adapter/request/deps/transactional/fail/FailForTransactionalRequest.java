package org.nikola.velemir.poshtar.guice.adapter.request.deps.transactional.fail;

import org.nikola.velemir.poshtar.core.request.Request;

public record FailForTransactionalRequest(String payload) implements Request<String> {
}
