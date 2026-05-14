package io.github.nikola_velemir.poshtar.validator.internal.rules.responsibility;

import io.github.nikola_velemir.poshtar.validator.internal.rules.Rule;

import java.util.List;

public class ResponsibilityRuleProvider {
    public static List<Rule> provide(){
        return List.of(
                new SingleResponsibilityHandlerRule()
        );
    }
}
