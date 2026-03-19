package org.nikola.velemir.poshtar.guice.adapter.request.deps.ping;

import org.nikola.velemir.poshtar.core.annotations.Handler;
import org.nikola.velemir.poshtar.core.request.handler.RequestHandler;

@Handler
public class PingRequestHandler implements RequestHandler<PingRequest,String> {
    @Override
    public String handle(PingRequest request) {
        return "Pong: " + request.message();
    }
}
