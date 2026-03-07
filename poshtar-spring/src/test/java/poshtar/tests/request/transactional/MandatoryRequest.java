package poshtar.tests.request.transactional;

import org.example.core.request.IRequest;

public record MandatoryRequest(String payload) implements IRequest<String> {
}
