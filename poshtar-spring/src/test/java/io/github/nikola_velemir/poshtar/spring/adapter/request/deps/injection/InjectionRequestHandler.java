package io.github.nikola_velemir.poshtar.spring.adapter.request.deps.injection;


import io.github.nikola_velemir.poshtar.core.annotations.Handler;
import io.github.nikola_velemir.poshtar.core.request.handler.RequestHandler;

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
