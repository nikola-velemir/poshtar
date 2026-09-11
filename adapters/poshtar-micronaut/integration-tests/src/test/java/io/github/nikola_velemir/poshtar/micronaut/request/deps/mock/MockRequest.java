package io.github.nikola_velemir.poshtar.micronaut.request.deps.mock;

import io.github.nikola_velemir.poshtar.core.request.Request;

public record MockRequest(String payload) implements Request<MockResponse> {
}
