package io.github.nikola_velemir.poshtar.guice.adapter.request.deps.chaining;

import io.github.nikola_velemir.poshtar.core.annotations.Handler;
import io.github.nikola_velemir.poshtar.core.mediator.Poshtar;
import io.github.nikola_velemir.poshtar.core.request.handler.RequestHandler;
import jakarta.inject.Inject;
import jakarta.inject.Provider;

@Handler
public class ChainingFirstRequestHandler implements RequestHandler<ChainingFirstRequest, ChainedResponse> {
    private final Provider<Poshtar> poshtarProvider;

    @Inject
    public ChainingFirstRequestHandler(Provider<Poshtar> poshtarProvider) {
        this.poshtarProvider = poshtarProvider;
    }

    @Override
    public ChainedResponse handle(ChainingFirstRequest chainingFirstRequest) {
        var poshtar = poshtarProvider.get();
        System.out.println("Executing Handler Hashcode: " + System.identityHashCode(this));
        var response = poshtar.send(new ChainingSecondRequest(1));
        return new ChainedResponse(response);
    }
}
