package io.github.nikola.velemir.poshtar.quarkus.it.component.request.deps.chaining.base;

import io.github.nikola_velemir.poshtar.core.annotations.Handler;
import io.github.nikola_velemir.poshtar.core.mediator.Poshtar;
import io.github.nikola_velemir.poshtar.core.request.handler.RequestHandler;
import jakarta.inject.Inject;

@Handler
public class ChainingFirstRequestHandler implements RequestHandler<ChainingFirstRequest, ChainedResponse> {
    @Inject
    Poshtar poshtar;

    public ChainingFirstRequestHandler(Poshtar poshtar) {
        this.poshtar = poshtar;
    }


    @Override
    public ChainedResponse handle(ChainingFirstRequest chainingFirstRequest) {
        var response = poshtar.send(new ChainingSecondRequest(1));
        return new ChainedResponse(response);
    }
}
