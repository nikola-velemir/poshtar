package org.nikola.velemir.poshtar.guice.adapter.request.deps.transactional.success;


import org.nikola.velemir.poshtar.core.request.Request;

public record TransactionalRequest(String payload) implements Request<String> {
}
