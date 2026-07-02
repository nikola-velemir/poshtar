package io.github.nikola.velemir.poshtar.quarkus.it.component.request.deps.chaining.mock;

import io.github.nikola_velemir.poshtar.core.annotations.Handler;
import io.github.nikola_velemir.poshtar.core.mediator.Poshtar;
import io.github.nikola_velemir.poshtar.core.request.handler.RequestHandler;
import jakarta.inject.Inject;

@Handler
public class MockChainedFirstRequestHandler implements RequestHandler<MockChainedFirstRequest, MockChainedResponse> {
    @Inject
    Poshtar poshtar;

    public MockChainedFirstRequestHandler(Poshtar poshtar) {
        this.poshtar = poshtar;
    }

    @Override
    public MockChainedResponse handle(MockChainedFirstRequest mockChainedFirstRequest) {
        var response = poshtar.send(new MockChainedSecondRequest("Hello"));
        return new MockChainedResponse(response);
    }
}
