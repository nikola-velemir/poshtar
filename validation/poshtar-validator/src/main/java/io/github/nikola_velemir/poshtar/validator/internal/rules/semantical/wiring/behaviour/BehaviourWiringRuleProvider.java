package io.github.nikola_velemir.poshtar.validator.internal.rules.semantical.wiring.behaviour;

import io.github.nikola_velemir.poshtar.validator.internal.rules.Rule;
import io.github.nikola_velemir.poshtar.validator.internal.rules.RuleKind;
import io.github.nikola_velemir.poshtar.validator.internal.rules.RuleProvider;

import java.util.List;

/**
 * Exposes Behaviour wiring rule
 * 
 */
public class BehaviourWiringRuleProvider implements RuleProvider {
    @Override
    public RuleKind getKind() {
        return RuleKind.SEMANTICAL;
    }
    /**
     * Provides the rule list
     */
    public List<Rule> provide(){
        return List.of(
                new BehaviourWiringRule()
        );
    }
}