package io.github.nikola_velemir.poshtar.guice.adapter.request.deps.chaining.mock;

import com.google.inject.Inject;
import io.github.nikola_velemir.poshtar.core.annotations.Handler;
import io.github.nikola_velemir.poshtar.core.mediator.Poshtar;
import io.github.nikola_velemir.poshtar.core.request.handler.RequestHandler;
import jakarta.inject.Provider;

@Handler
public class MockChainedFirstRequestHandler implements RequestHandler<MockChainedFirstRequest, MockChainedResponse> {

    private final Provider<Poshtar> poshtarProvider;

    @Inject
    public MockChainedFirstRequestHandler(Provider<Poshtar> poshtarProvider) {
        this.poshtarProvider = poshtarProvider;
    }

    @Override
    public MockChainedResponse handle(MockChainedFirstRequest mockChainedFirstRequest) {
        var poshtar = poshtarProvider.get();
        var response = poshtar.send(new MockChainedSecondRequest("Hello"));
        return new MockChainedResponse(response);
    }
}
