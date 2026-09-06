package io.github.nikola_velemir.poshtar.guice.adapter.pipeline.deps.mock.basic;

import io.github.nikola_velemir.poshtar.core.annotations.Handler;
import io.github.nikola_velemir.poshtar.core.request.handler.RequestHandler;

@Handler
public class BasicMockRequestHandler implements RequestHandler<BasicMockRequest,String> {
    @Override
    public String handle(BasicMockRequest basicMockRequest) {
        return "Basic";
    }
}
