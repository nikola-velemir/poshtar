package poshtar.tests.request.deps.injection;

import org.nikola.velemir.poshtar.core.annotations.Handler;
import org.nikola.velemir.poshtar.core.request.handler.RequestHandler;

@Handler
public class InjectionRequestHandler implements RequestHandler<InjectionRequest, String> {
    private final poshtar.tests.request.deps.injection.DummyLoggingService loggingService;

    public InjectionRequestHandler(poshtar.tests.request.deps.injection.DummyLoggingService loggingService) {
        this.loggingService = loggingService;
    }

    @Override
    public String handle(poshtar.tests.request.deps.injection.InjectionRequest injectionRequest) {
        String logResult = loggingService.log(injectionRequest.payload());
        return "Request with " + logResult;
    }
}
