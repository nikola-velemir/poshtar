package fixtures.rules.architectural.noInjection.handler.request;

import io.github.nikola_velemir.poshtar.validator.api.annotations.injection.OverruleNoInjection;
@OverruleNoInjection
public class ValidRequestConsumer{
    private final InjectedRequestHandler injected = new InjectedRequestHandler();
}