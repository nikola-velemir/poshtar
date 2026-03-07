package poshtar.tests.request.injection;

import org.example.core.request.IRequest;

public record InjectionRequest(String payload) implements IRequest<String> {
}
