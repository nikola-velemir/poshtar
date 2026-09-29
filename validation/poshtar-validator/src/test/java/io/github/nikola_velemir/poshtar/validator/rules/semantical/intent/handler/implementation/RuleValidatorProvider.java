package io.github.nikola_velemir.poshtar.validator.rules.semantical.intent.handler.implementation;

import io.github.nikola_velemir.poshtar.validator.internal.context.ProcessorContext;
import io.github.nikola_velemir.poshtar.validator.internal.rules.RuleValidator;
import io.github.nikola_velemir.poshtar.validator.internal.rules.semantical.intent.handler.implementation.RequestHandlerImplementationRuleProvider;

import javax.annotation.processing.RoundEnvironment;

class RuleValidatorProvider {
    public static class Implementation implements RuleValidator {
        @Override
        public void validateRules(RoundEnvironment roundEnv, ProcessorContext ctx) {
            var rules = new RequestHandlerImplementationRuleProvider().provide();
            rules.
                    forEach(s -> s.validate(roundEnv, ctx));
        }
    }
}
