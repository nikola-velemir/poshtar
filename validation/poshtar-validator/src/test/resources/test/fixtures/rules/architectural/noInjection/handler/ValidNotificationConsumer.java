package test.fixtures.rules.architectural.noInjection.handler;

import io.github.nikola_velemir.poshtar.validator.api.annotations.injection.OverruleNoInjection;

@OverruleNoInjection
public class ValidNotificationConsumer {
    private final InjectedNotificationHandler injected = new InjectedNotificationHandler();

}
