package io.github.nikola_velemir.poshtar.spring.adapter.request.deps.transactional.mandatory;


import io.github.nikola_velemir.poshtar.core.request.Request;

public record MandatoryRequest(String payload) implements Request<String> {
}
