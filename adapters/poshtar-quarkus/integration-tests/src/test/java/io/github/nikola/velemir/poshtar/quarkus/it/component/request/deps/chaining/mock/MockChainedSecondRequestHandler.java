package io.github.nikola.velemir.poshtar.quarkus.it.component.request.deps.chaining.mock;

import io.github.nikola_velemir.poshtar.core.annotations.Handler;
import io.github.nikola_velemir.poshtar.core.request.handler.RequestHandler;

@Handler
public class MockChainedSecondRequestHandler implements RequestHandler<MockChainedSecondRequest, String> {
    @Override
    public String handle(MockChainedSecondRequest mockChainedSecondRequest) {
        return "Hello";
    }
}
