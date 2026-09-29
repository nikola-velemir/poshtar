package io.github.nikola_velemir.poshtar.validator.rules.semantical.intent.reqeust;

import io.github.nikola_velemir.poshtar.validator.internal.context.ProcessorContext;
import io.github.nikola_velemir.poshtar.validator.internal.rules.RuleValidator;
import io.github.nikola_velemir.poshtar.validator.internal.rules.semantical.intent.request.RequestIntentRuleProvider;

import javax.annotation.processing.RoundEnvironment;

class RuleValidatorProvider {
    public static class HandlerIntent implements RuleValidator {
        @Override
        public void validateRules(RoundEnvironment roundEnv, ProcessorContext ctx) {
            var rules = new RequestIntentRuleProvider().provide();
            rules.
                    forEach(s -> s.validate(roundEnv, ctx));
        }
    }
}
