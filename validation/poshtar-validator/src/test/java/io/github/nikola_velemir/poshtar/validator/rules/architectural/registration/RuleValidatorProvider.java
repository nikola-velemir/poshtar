package io.github.nikola_velemir.poshtar.validator.rules.architectural.registration;

import io.github.nikola_velemir.poshtar.validator.internal.context.ProcessorContext;
import io.github.nikola_velemir.poshtar.validator.internal.rules.RuleValidator;
import io.github.nikola_velemir.poshtar.validator.internal.rules.architectural.noInjection.BehaviourNoInjectionRuleProvider;
import io.github.nikola_velemir.poshtar.validator.internal.rules.architectural.noInjection.HandlerNoInjectionRuleProvider;
import io.github.nikola_velemir.poshtar.validator.internal.rules.architectural.registration.AmbiguityRuleProvider;
import io.github.nikola_velemir.poshtar.validator.internal.rules.architectural.registration.OrphanRequestRuleProvider;

import javax.annotation.processing.RoundEnvironment;

public class RuleValidatorProvider {
    public static class Ambiguity implements RuleValidator {

        @Override
        public void validateRules(RoundEnvironment roundEnv, ProcessorContext ctx) {
            var rules = new AmbiguityRuleProvider().provide();
            rules.
                    forEach(s -> s.validate(roundEnv, ctx));
        }
    }

    public static class Orphan implements RuleValidator {
        @Override
        public void validateRules(RoundEnvironment roundEnv, ProcessorContext ctx) {
            var rules = new OrphanRequestRuleProvider().provide();
            rules.
                    forEach(s -> s.validate(roundEnv, ctx));
        }
    }
}
