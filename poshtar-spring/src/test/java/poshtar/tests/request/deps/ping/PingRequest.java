package poshtar.tests.request.deps.ping;

import org.example.core.request.IRequest;

public record PingRequest (String message) implements IRequest<String> {
}
