package io.github.nikola_velemir.poshtar.validator.rules.semantical.returnTypes;

import io.github.nikola_velemir.poshtar.validator.internal.context.ProcessorContext;
import io.github.nikola_velemir.poshtar.validator.internal.rules.RuleValidator;
import io.github.nikola_velemir.poshtar.validator.internal.rules.semantical.responsibility.ResponsibilityRuleProvider;
import io.github.nikola_velemir.poshtar.validator.internal.rules.semantical.returnTypes.ReturnTypesRuleProvider;

import javax.annotation.processing.RoundEnvironment;

class RuleValidatorProvider {
    public static class Primitives implements RuleValidator {
        @Override
        public void validateRules(RoundEnvironment roundEnv, ProcessorContext ctx) {
            var rules = new ReturnTypesRuleProvider().provide();
            rules.
                    forEach(s -> s.validate(roundEnv, ctx));
        }
    }
}
