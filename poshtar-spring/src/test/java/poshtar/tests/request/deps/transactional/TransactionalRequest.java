package poshtar.tests.request.deps.transactional;


import org.nikola.velemir.poshtar.core.request.IRequest;

public record TransactionalRequest(String payload) implements IRequest<String> {
}
