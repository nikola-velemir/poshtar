package fixtures.rules.architectural.noInjection.handler;

public class FailingRequestConsumer {
    private final InjectedRequestHandler injected = new InjectedRequestHandler();
}
