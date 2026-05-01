package io.github.nikola_velemir.poshtar.spring.adapter.request.deps.transactional.basic;


import io.github.nikola_velemir.poshtar.core.request.Request;

public record TransactionalRequest(String payload) implements Request<String> {
}
