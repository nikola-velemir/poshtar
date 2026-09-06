package io.github.nikola_velemir.poshtar.spring.adapter.request.deps.chaining.base;

import io.github.nikola_velemir.poshtar.core.annotations.Handler;
import io.github.nikola_velemir.poshtar.core.mediator.Poshtar;
import io.github.nikola_velemir.poshtar.core.request.handler.RequestHandler;
import org.springframework.beans.factory.annotation.Autowired;

@Handler
public class ChainingFirstRequestHandler implements RequestHandler<ChainingFirstRequest, ChainedResponse> {
    @Autowired
    private final Poshtar poshtar;

    public ChainingFirstRequestHandler(Poshtar poshtar) {
        this.poshtar = poshtar;
    }


    @Override
    public ChainedResponse handle(ChainingFirstRequest chainingFirstRequest) {
        var response = poshtar.send(new ChainingSecondRequest(1));
        return new ChainedResponse(response);
    }
}
