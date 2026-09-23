package io.github.nikola_velemir.poshtar.validator.internal.rules.architectural.finality;

import io.github.nikola_velemir.poshtar.validator.internal.rules.Rule;
import io.github.nikola_velemir.poshtar.validator.internal.rules.RuleKind;
import io.github.nikola_velemir.poshtar.validator.internal.rules.RuleProvider;

import java.util.List;

/**
 * Exposes notification finality rule
 */
public class NotificationFinalityRuleProvider implements RuleProvider {
    @Override
    public RuleKind getKind() {
        return RuleKind.ARCHITECTURAL;
    }

    @Override
    public List<Rule> provide() {
        return List.of(new NotificationFinalityRule());
    }
}
