package io.github.nikola_velemir.poshtar.micronaut.request.deps.chaining.mock;

import io.github.nikola_velemir.poshtar.core.annotations.Handler;
import io.github.nikola_velemir.poshtar.core.mediator.Poshtar;
import io.github.nikola_velemir.poshtar.core.request.handler.RequestHandler;
import jakarta.inject.Inject;
import jakarta.inject.Provider;

@Handler
public class MockChainedFirstRequestHandler implements RequestHandler<MockChainedFirstRequest, MockChainedResponse> {

    @Inject
    private final Provider<Poshtar> poshtarProvider;

    public MockChainedFirstRequestHandler(Provider<Poshtar> poshtar) {
        this.poshtarProvider = poshtar;
    }

    @Override
    public MockChainedResponse handle(MockChainedFirstRequest mockChainedFirstRequest) {
        var response =poshtarProvider.get().send(new MockChainedSecondRequest("Hello"));
        return new MockChainedResponse(response);
    }
}
