package poshtar.tests.request.deps.ping;


import org.nikola.velemir.poshtar.core.annotations.RequestHandler;
import org.nikola.velemir.poshtar.core.request.handler.IRequestHandler;

@RequestHandler
public class PingRequestHandler implements IRequestHandler<PingRequest,String> {
    @Override
    public String handle(PingRequest request) {
        return "Pong: " + request.message();
    }
}
