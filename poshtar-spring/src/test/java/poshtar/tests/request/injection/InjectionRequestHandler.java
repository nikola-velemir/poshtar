package poshtar.tests.request.injection;

import org.example.core.annotations.RequestHandler;
import org.example.core.request.handler.IRequestHandler;

@RequestHandler
public class InjectionRequestHandler implements IRequestHandler<InjectionRequest, String> {
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
