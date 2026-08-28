package io.github.nikola_velemir.poshtar.validator.semantics.internal.rules;

import io.github.nikola_velemir.poshtar.validator.semantics.internal.rules.responsibility.ResponsibilityRuleProvider;
import io.github.nikola_velemir.poshtar.validator.semantics.internal.rules.returnTypes.ReturnTypesRuleProvider;
import io.github.nikola_velemir.poshtar.validator.semantics.internal.rules.wiring.WiringRuleProvider;
import io.github.nikola_velemir.poshtar.validator.base.internal.context.ProcessorContext;
import io.github.nikola_velemir.poshtar.validator.base.internal.rules.Rule;
import io.github.nikola_velemir.poshtar.validator.base.internal.rules.RuleValidator;
import io.github.nikola_velemir.poshtar.validator.base.internal.rules.RuleValidatorProvider;

import javax.annotation.processing.RoundEnvironment;
import java.util.List;
import java.util.stream.Stream;

public class ValidatorRuleProvider implements RuleValidatorProvider {
    public static RuleValidator provide(){
        return new ValidatorRuleProvider().provideValidator();
    }
    @Override
    public RuleValidator provideValidator() {
        return new RuleValidatorImpl();
    }
    static class RuleValidatorImpl implements RuleValidator {
        private static final List<Rule> RULES = provideRules();

        private static List<Rule> provideRules(){
            return Stream.of(
                    ResponsibilityRuleProvider.provide(),
                    ReturnTypesRuleProvider.provide(),
                    WiringRuleProvider.provide()
            ).flatMap(List::stream
            ).toList();
        }

        public void validateRules(RoundEnvironment roundEnv, ProcessorContext ctx) {
            for (Rule rule : RULES) {
                rule.validate(roundEnv, ctx);
            }
        }
    }
}
