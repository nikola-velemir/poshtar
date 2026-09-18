package test.fixtures.rules.architectural.noInjection.handler.notification;

public class FailingNotificationConsumer {
    private final InjectedNotificationHandler injected = new InjectedNotificationHandler();

}
