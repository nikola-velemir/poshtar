package test.fixtures.rules.architectural.noInjection.behaviour;

import io.github.nikola_velemir.poshtar.validator.api.annotations.injection.OverruleNoInjection;

@OverruleNoInjection
public class InjectedRequestValidConsumer {
    InjectedRequestBehaviour behaviour = new InjectedRequestBehaviour();
}