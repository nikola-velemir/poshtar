package poshtar.tests.request.deps.transactional;


import org.nikola.velemir.poshtar.core.request.IRequest;

public record MandatoryRequest(String payload) implements IRequest<String> {
}
