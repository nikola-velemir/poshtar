package poshtar.tests.request.deps.transactional;


import org.nikola.velemir.poshtar.core.request.Request;

public record MandatoryRequest(String payload) implements Request<String> {
}
