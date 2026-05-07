package io.github.nikola_velemir.poshtar.guice.adapter.request.deps.transactional.success;


import io.github.nikola_velemir.poshtar.core.request.Request;

public record TransactionalRequest(String payload) implements Request<String> {
}
