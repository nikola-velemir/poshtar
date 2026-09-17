package io.github.nikola_velemir.poshtar.validator.rules.semantical.responsiblity;

import io.github.nikola_velemir.poshtar.validator.internal.context.ProcessorContext;
import io.github.nikola_velemir.poshtar.validator.internal.rules.Rule;
import io.github.nikola_velemir.poshtar.validator.internal.rules.RuleValidator;
import io.github.nikola_velemir.poshtar.validator.internal.rules.architectural.registration.AmbiguityRuleProvider;
import io.github.nikola_velemir.poshtar.validator.internal.rules.architectural.registration.OrphanRequestRuleProvider;
import io.github.nikola_velemir.poshtar.validator.internal.rules.semantical.responsibility.ResponsibilityRuleProvider;

import javax.annotation.processing.RoundEnvironment;

class RuleValidatorProvider {
    public static class SingleResponsibility implements RuleValidator {
        @Override
        public void validateRules(RoundEnvironment roundEnv, ProcessorContext ctx) {
            var rules = new ResponsibilityRuleProvider().provide();
            rules.
                    forEach(s -> s.validate(roundEnv, ctx));
        }
    }
}
