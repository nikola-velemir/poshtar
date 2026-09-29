package io.github.nikola_velemir.poshtar.validator.internal.rules.semantical.intent.handler.implementation;

import io.github.nikola_velemir.poshtar.validator.internal.rules.Rule;
import io.github.nikola_velemir.poshtar.validator.internal.rules.RuleKind;
import io.github.nikola_velemir.poshtar.validator.internal.rules.RuleProvider;

import java.util.List;

public class RequestHandlerImplementationRuleProvider implements RuleProvider {


    @Override
    public RuleKind getKind() {
        return RuleKind.SEMANTICAL;
    }

    @Override
    public List<Rule> provide() {
        return List.of(new RequestHandlerImplementationRule());
    }
}
