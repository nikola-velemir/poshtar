package poshtar.tests.request.deps.transactional;

import org.example.core.request.IRequest;

public record MandatoryRequest(String payload) implements IRequest<String> {
}
