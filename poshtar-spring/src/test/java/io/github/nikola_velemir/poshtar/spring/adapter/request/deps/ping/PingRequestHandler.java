package io.github.nikola_velemir.poshtar.spring.adapter.request.deps.ping;


import io.github.nikola_velemir.poshtar.core.annotations.Handler;
import io.github.nikola_velemir.poshtar.core.request.handler.RequestHandler;

@Handler
public class PingRequestHandler implements RequestHandler<PingRequest,String> {
    @Override
    public String handle(PingRequest request) {
        return "Pong: " + request.message();
    }
}
