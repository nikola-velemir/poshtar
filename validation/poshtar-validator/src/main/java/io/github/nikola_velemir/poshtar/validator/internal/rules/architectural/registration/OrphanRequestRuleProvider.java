package io.github.nikola_velemir.poshtar.validator.internal.rules.architectural.registration;

import io.github.nikola_velemir.poshtar.validator.internal.rules.Rule;
import io.github.nikola_velemir.poshtar.validator.internal.rules.RuleKind;
import io.github.nikola_velemir.poshtar.validator.internal.rules.RuleProvider;

import java.util.List;

/**
 * Exposes oprhan request rule
 */
public class OrphanRequestRuleProvider implements RuleProvider {
    @Override
    public RuleKind getKind() {
        return RuleKind.ARCHITECTURAL;
    }

    public List<Rule> provide() {
        return List.of(
                new OrphanRequestRule()
        );
    }
}