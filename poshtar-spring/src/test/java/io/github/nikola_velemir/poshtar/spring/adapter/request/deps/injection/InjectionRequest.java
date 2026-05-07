package io.github.nikola_velemir.poshtar.spring.adapter.request.deps.injection;


import io.github.nikola_velemir.poshtar.core.request.Request;

public record InjectionRequest(String payload) implements Request<String> {
}
