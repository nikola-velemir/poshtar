package poshtar.tests.request.deps.injection;

import org.nikola.velemir.poshtar.core.annotations.RequestHandler;
import org.nikola.velemir.poshtar.core.request.handler.IRequestHandler;

@RequestHandler
public class InjectionRequestHandler implements IRequestHandler<InjectionRequest, String> {
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
