package org.nikola.velemir.poshtar.spring.adapter.request.deps.injection;

import org.nikola.velemir.poshtar.core.annotations.Handler;
import org.nikola.velemir.poshtar.core.request.handler.RequestHandler;

@Handler
public class InjectionRequestHandler implements RequestHandler<InjectionRequest, String> {
    private final DummyLoggingService loggingService;

    public InjectionRequestHandler(DummyLoggingService loggingService) {
        this.loggingService = loggingService;
    }

    @Override
    public String handle(InjectionRequest injectionRequest) {
        String logResult = loggingService.log(injectionRequest.payload());
        return "Request with " + logResult;
    }
}
