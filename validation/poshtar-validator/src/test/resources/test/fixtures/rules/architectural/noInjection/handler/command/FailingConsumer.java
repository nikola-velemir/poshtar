package test.fixtures.rules.architectural.noInjection.handler.command;

public class FailingConsumer {
    private InjectedHandler handler = new InjectedHandler();
}
