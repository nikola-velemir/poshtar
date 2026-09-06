package io.github.nikola_velemir.poshtar.guice.adapter.request.deps.mock;

import io.github.nikola_velemir.poshtar.core.annotations.Handler;
import io.github.nikola_velemir.poshtar.core.request.handler.RequestHandler;

@Handler
public class MockRequestHandler implements RequestHandler<MockRequest, MockResponse> {
    @Override
    public MockResponse handle(MockRequest mockRequest) {
        var payload = mockRequest.payload();
        return new MockResponse(String.format("[PoshtaR] %s", payload));
    }
}
