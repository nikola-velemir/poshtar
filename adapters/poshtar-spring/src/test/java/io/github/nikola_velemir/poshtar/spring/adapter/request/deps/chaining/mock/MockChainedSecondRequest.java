package io.github.nikola_velemir.poshtar.spring.adapter.request.deps.chaining.mock;

import io.github.nikola_velemir.poshtar.core.request.Request;

public record MockChainedSecondRequest(String payload) implements Request<String> {
}
