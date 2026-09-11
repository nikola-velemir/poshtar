package io.github.nikola_velemir.poshtar.micronaut.request.deps.chaining.base;

import io.github.nikola_velemir.poshtar.core.annotations.Handler;
import io.github.nikola_velemir.poshtar.core.mediator.Poshtar;
import io.github.nikola_velemir.poshtar.core.request.handler.RequestHandler;
import jakarta.inject.Inject;
import jakarta.inject.Provider;

@Handler
public class ChainingFirstRequestHandler implements RequestHandler<ChainingFirstRequest, ChainedResponse> {
    @Inject
    private final Provider<Poshtar> poshtarProvider;

    public ChainingFirstRequestHandler(Provider<Poshtar> poshtarProvider) {
        this.poshtarProvider = poshtarProvider;
    }

    @Override
    public ChainedResponse handle(ChainingFirstRequest request) {
        var response = poshtarProvider.get().send(new ChainingSecondRequest(1));
        return new ChainedResponse(response);
    }
}
