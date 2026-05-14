package io.github.nikola_velemir.poshtar.validator.internal.rules.noInjection;

import io.github.nikola_velemir.poshtar.validator.internal.rules.Rule;

import java.util.List;

public class NoInjectionRuleProvider {
    public static List<Rule> provide(){
        return List.of(
                new BehaviourNoInjectionRule(),
                new HandlerNoInjectionRule()
        );
    }
}
