package org.nikola.velemir.poshtar.spring.adapter.request.deps.injection;


import org.nikola.velemir.poshtar.core.request.Request;

public record InjectionRequest(String payload) implements Request<String> {
}
