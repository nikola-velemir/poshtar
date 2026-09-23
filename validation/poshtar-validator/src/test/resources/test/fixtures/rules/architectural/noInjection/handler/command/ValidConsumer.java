package test.fixtures.rules.architectural.noInjection.handler.command;

import io.github.nikola_velemir.poshtar.validator.api.annotations.injection.OverruleNoInjection;

@OverruleNoInjection
public class ValidConsumer {
    private InjectedHandler handler = new InjectedHandler();
}
