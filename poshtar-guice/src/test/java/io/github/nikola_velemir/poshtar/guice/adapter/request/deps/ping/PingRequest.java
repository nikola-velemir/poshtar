package io.github.nikola_velemir.poshtar.guice.adapter.request.deps.ping;

import io.github.nikola_velemir.poshtar.core.request.Request;

public record PingRequest (String message) implements Request<String> {
}
