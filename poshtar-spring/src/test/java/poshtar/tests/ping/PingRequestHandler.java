package poshtar.tests.ping;

import org.example.core.annotations.RequestHandler;
import org.example.core.request.handler.IRequestHandler;

@RequestHandler
public class PingRequestHandler implements IRequestHandler<PingRequest,String> {
    @Override
    public String handle(PingRequest request) {
        return "Pong: " + request.message();
    }
}
