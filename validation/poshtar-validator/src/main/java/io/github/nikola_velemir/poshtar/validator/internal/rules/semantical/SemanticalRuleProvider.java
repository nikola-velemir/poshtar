package io.github.nikola_velemir.poshtar.validator.internal.rules.semantical;

import io.github.nikola_velemir.poshtar.validator.internal.rules.Rule;
import io.github.nikola_velemir.poshtar.validator.internal.rules.RuleKind;
import io.github.nikola_velemir.poshtar.validator.internal.rules.RuleProvider;
import io.github.nikola_velemir.poshtar.validator.internal.rules.semantical.responsibility.ResponsibilityRuleProvider;
import io.github.nikola_velemir.poshtar.validator.internal.rules.semantical.returnTypes.ReturnTypesRuleProvider;
import io.github.nikola_velemir.poshtar.validator.internal.rules.semantical.wiring.WiringRuleProvider;

import java.util.List;
import java.util.stream.Stream;

public class SemanticalRuleProvider implements RuleProvider {
    public  List<Rule> provide() {
        return Stream.of(
                       new WiringRuleProvider().provide(),
                        new ReturnTypesRuleProvider().provide(),
                        new ResponsibilityRuleProvider().provide()
                ).flatMap(List::stream)
                .toList();
    }

    public  RuleKind getKind() {
        return RuleKind.SEMANTICAL;
    }

}

