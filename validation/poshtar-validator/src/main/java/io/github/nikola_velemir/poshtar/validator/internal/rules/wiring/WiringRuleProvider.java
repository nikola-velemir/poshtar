package io.github.nikola_velemir.poshtar.validator.internal.rules.wiring;

import io.github.nikola_velemir.poshtar.validator.internal.rules.Rule;

import java.util.List;

public class WiringRuleProvider {
    public static List<Rule> provide(){
        return List.of(
               new HandlerWiringRule(),
                new BehaviourWiringRule()
        );
    }
}
