package poshtar.tests.request.deps.transactional;


import org.nikola.velemir.poshtar.core.request.Request;

public record TransactionalRequest(String payload) implements Request<String> {
}
