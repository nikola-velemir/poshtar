package fixtures.rules.architectural.noInjection.handler.request;

public class FailingRequestConsumer {
    private final InjectedRequestHandler injected = new InjectedRequestHandler();
}
