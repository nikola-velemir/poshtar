package io.github.nikola_velemir.poshtar.validator.rules.semantical.intent.handler;

import io.github.nikola_velemir.poshtar.validator.internal.context.ProcessorContext;
import io.github.nikola_velemir.poshtar.validator.internal.rules.RuleValidator;
import io.github.nikola_velemir.poshtar.validator.internal.rules.semantical.intent.handler.RequestHandlerIntentRuleProvider;
import io.github.nikola_velemir.poshtar.validator.internal.rules.semantical.responsibility.ResponsibilityRuleProvider;

import javax.annotation.processing.RoundEnvironment;

class RuleValidatorProvider {
    public static class HandlerIntent implements RuleValidator {
        @Override
        public void validateRules(RoundEnvironment roundEnv, ProcessorContext ctx) {
            var rules = new RequestHandlerIntentRuleProvider().provide();
            rules.
                    forEach(s -> s.validate(roundEnv, ctx));
        }
    }
}
