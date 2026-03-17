package poshtar.tests.request.deps.ping;


import org.nikola.velemir.poshtar.core.request.IRequest;

public record PingRequest (String message) implements IRequest<String> {
}
