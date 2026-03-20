package org.nikola.velemir.poshtar.spring.adapter.request.deps.transactional.mandatory;


import org.nikola.velemir.poshtar.core.request.Request;

public record MandatoryRequest(String payload) implements Request<String> {
}
