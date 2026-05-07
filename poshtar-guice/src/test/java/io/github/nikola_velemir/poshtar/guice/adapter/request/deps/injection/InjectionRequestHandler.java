package io.github.nikola_velemir.poshtar.guice.adapter.request.deps.injection;

import jakarta.inject.Inject;
import io.github.nikola_velemir.poshtar.core.annotations.Handler;
import io.github.nikola_velemir.poshtar.core.request.handler.RequestHandler;


@Handler
public class InjectionRequestHandler implements RequestHandler<InjectionRequest, InjectionResponse> {
    private final DummyLoggingService loggingService;

    @Inject
    public InjectionRequestHandler(DummyLoggingService loggingService) {
        this.loggingService = loggingService;
    }


    @Override
    public InjectionResponse handle(InjectionRequest injectionRequest) {
        String logResult = loggingService.log(injectionRequest.payload);
        return  new InjectionResponse("Request with " + logResult);
    }
}
