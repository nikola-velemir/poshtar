package poshtar.tests.request.deps.injection;

import org.example.core.request.IRequest;

public record InjectionRequest(String payload) implements IRequest<String> {
}
