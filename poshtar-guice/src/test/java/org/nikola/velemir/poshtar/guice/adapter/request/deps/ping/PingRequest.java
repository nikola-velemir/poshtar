package org.nikola.velemir.poshtar.guice.adapter.request.deps.ping;

import org.nikola.velemir.poshtar.core.request.Request;

public record PingRequest (String message) implements Request<String> {
}
