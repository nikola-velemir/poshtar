package io.github.nikola_velemir.poshtar.validator.internal.rules.semantical.wiring.handler;

import io.github.nikola_velemir.poshtar.validator.internal.rules.Rule;
import io.github.nikola_velemir.poshtar.validator.internal.rules.RuleKind;
import io.github.nikola_velemir.poshtar.validator.internal.rules.RuleProvider;

import java.util.List;

public class HandlerWiringRuleProvider implements RuleProvider {
    @Override
    public RuleKind getKind() {
        return RuleKind.SEMANTICAL;
    }

    public List<Rule> provide(){
        return List.of(
                new HandlerWiringRule()
        );
    }
}