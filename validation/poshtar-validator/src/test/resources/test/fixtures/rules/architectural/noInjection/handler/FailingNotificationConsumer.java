package test.fixtures.rules.architectural.noInjection.handler;

public class FailingNotificationConsumer {
    private final InjectedNotificationHandler injected = new InjectedNotificationHandler();

}
