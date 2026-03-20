package org.nikola.velemir.poshtar.spring.adapter.request.deps.ping;

import org.nikola.velemir.poshtar.core.request.Request;

public record PingRequest (String message) implements Request<String> {
}
