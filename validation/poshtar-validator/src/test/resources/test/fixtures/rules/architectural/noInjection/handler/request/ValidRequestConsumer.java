package test.fixtures.rules.architectural.noInjection.handler.request;

import io.github.nikola_velemir.poshtar.validator.api.annotations.injection.OverruleNoInjection;

@OverruleNoInjection
public class ValidRequestConsumer {
    InjectedRequestHandler handler = new InjectedRequestHandler();
}
