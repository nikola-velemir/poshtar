package io.github.nikola_velemir.poshtar.validator.rules.architectural.finality;

import io.github.nikola_velemir.poshtar.validator.internal.context.ProcessorContext;
import io.github.nikola_velemir.poshtar.validator.internal.rules.RuleValidator;
import io.github.nikola_velemir.poshtar.validator.internal.rules.architectural.finality.NotificationFinalityRuleProvider;
import io.github.nikola_velemir.poshtar.validator.internal.rules.architectural.finality.RequestFinalityRuleProvider;

import javax.annotation.processing.RoundEnvironment;

public class RuleValidatorProvider  {
    public static class Request implements RuleValidator{

        @Override
        public void validateRules(RoundEnvironment roundEnv, ProcessorContext ctx) {
            var rules = new RequestFinalityRuleProvider().provide();
            rules.
                    forEach(s -> s.validate(roundEnv, ctx));
        }
    }
    public static class Notification  implements RuleValidator{
        @Override
        public void validateRules(RoundEnvironment roundEnv, ProcessorContext ctx) {
            var rules = new NotificationFinalityRuleProvider().provide();
            rules.
                    forEach(s -> s.validate(roundEnv, ctx));
        }
    }
}
