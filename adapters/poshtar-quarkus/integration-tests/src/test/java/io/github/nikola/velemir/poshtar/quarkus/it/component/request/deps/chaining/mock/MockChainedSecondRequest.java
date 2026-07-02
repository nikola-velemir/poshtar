package io.github.nikola.velemir.poshtar.quarkus.it.component.request.deps.chaining.mock;

import io.github.nikola_velemir.poshtar.core.request.Request;

public record MockChainedSecondRequest(String payload) implements Request<String> {
}
