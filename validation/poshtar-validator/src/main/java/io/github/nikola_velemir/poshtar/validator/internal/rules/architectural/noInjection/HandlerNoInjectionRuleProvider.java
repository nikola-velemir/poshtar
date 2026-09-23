package io.github.nikola_velemir.poshtar.validator.internal.rules.architectural.noInjection;

import io.github.nikola_velemir.poshtar.validator.internal.rules.Rule;
import io.github.nikola_velemir.poshtar.validator.internal.rules.RuleKind;
import io.github.nikola_velemir.poshtar.validator.internal.rules.RuleProvider;

import java.util.List;

/**
 * Exposes handler no injection rule
 */
public class HandlerNoInjectionRuleProvider implements RuleProvider {
    @Override
    public RuleKind getKind() {
        return RuleKind.ARCHITECTURAL;
    }

    @Override
    public List<Rule> provide() {
        return List.of(new HandlerNoInjectionRule());
    }
}
