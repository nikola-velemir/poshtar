package io.github.nikola_velemir.poshtar.spring.adapter.request.deps.chaining.mock;

import io.github.nikola_velemir.poshtar.core.annotations.Handler;
import io.github.nikola_velemir.poshtar.core.mediator.Poshtar;
import io.github.nikola_velemir.poshtar.core.request.handler.RequestHandler;
import org.springframework.beans.factory.annotation.Autowired;

@Handler
public class MockChainedFirstRequestHandler implements RequestHandler<MockChainedFirstRequest, MockChainedResponse> {

    @Autowired
    private final Poshtar poshtar;

    public MockChainedFirstRequestHandler(Poshtar poshtar) {
        this.poshtar = poshtar;
    }

    @Override
    public MockChainedResponse handle(MockChainedFirstRequest mockChainedFirstRequest) {
        var response = poshtar.send(new MockChainedSecondRequest("Hello"));
        return new MockChainedResponse(response);
    }
}
