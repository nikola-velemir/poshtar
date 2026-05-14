package io.github.nikola_velemir.poshtar.validator.internal.rules.finality;

import io.github.nikola_velemir.poshtar.validator.internal.rules.Rule;

import java.util.List;

public class FinalityRuleProvider {
    public static List<Rule> provide() {
        return List.of(new NotificationFinalityRule(), new RequestFinalityRule());
    }
}
