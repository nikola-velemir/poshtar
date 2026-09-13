package io.github.nikola_velemir.poshtar.micronaut.it.request.deps.mock;

import io.github.nikola_velemir.poshtar.core.request.Request;

public record MockRequest(String payload) implements Request<MockResponse> {
}
