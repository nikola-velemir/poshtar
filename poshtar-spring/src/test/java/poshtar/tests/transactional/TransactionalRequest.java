package poshtar.tests.transactional;

import org.example.core.request.IRequest;

public record TransactionalRequest(String payload) implements IRequest<String> {
}
