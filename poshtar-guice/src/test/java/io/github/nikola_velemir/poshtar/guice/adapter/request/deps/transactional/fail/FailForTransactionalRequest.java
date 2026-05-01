package io.github.nikola_velemir.poshtar.guice.adapter.request.deps.transactional.fail;

import io.github.nikola_velemir.poshtar.core.request.Request;

public record FailForTransactionalRequest(String payload) implements Request<String> {
}
