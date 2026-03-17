package poshtar.tests.request.deps.injection;

import org.nikola.velemir.poshtar.core.request.IRequest;

public record InjectionRequest(String payload) implements IRequest<String> {
}
