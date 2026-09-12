package io.github.nikola_velemir.poshtar.validator.internal.rules.architectural;

import io.github.nikola_velemir.poshtar.validator.internal.rules.Rule;
import io.github.nikola_velemir.poshtar.validator.internal.rules.RuleKind;
import io.github.nikola_velemir.poshtar.validator.internal.rules.RuleProvider;
import io.github.nikola_velemir.poshtar.validator.internal.rules.architectural.deadPipeline.DeadPipelineRuleProvider;
import io.github.nikola_velemir.poshtar.validator.internal.rules.architectural.finality.FinalityRuleProvider;
import io.github.nikola_velemir.poshtar.validator.internal.rules.architectural.noInjection.NoInjectionRuleProvider;
import io.github.nikola_velemir.poshtar.validator.internal.rules.architectural.registration.RegistrationRuleProvider;

import java.util.List;
import java.util.stream.Stream;

public class ArchitecturalRuleProvider implements RuleProvider {
    public  List<Rule> provide() {
        return Stream.of(
                     new   RegistrationRuleProvider().provide(),
                       new NoInjectionRuleProvider().provide(),
                       new DeadPipelineRuleProvider().provide(),
                       new FinalityRuleProvider().provide()
                ).flatMap(List::stream)
                .toList();
    }

    public  RuleKind getKind() {
        return RuleKind.ARCHITECTURAL;
    }
}
