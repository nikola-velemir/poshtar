package io.github.nikola_velemir.poshtar.guice.adapter.request.deps.chaining.mock;

import io.github.nikola_velemir.poshtar.core.request.Request;

public record MockChainedSecondRequest(String payload) implements Request<String> {
}
