package io.github.nikola_velemir.poshtar.validator.rules.architectural.noInjection;

import io.github.nikola_velemir.poshtar.validator.internal.context.ProcessorContext;
import io.github.nikola_velemir.poshtar.validator.internal.rules.RuleValidator;
import io.github.nikola_velemir.poshtar.validator.internal.rules.architectural.noInjection.BehaviourNoInjectionRuleProvider;
import io.github.nikola_velemir.poshtar.validator.internal.rules.architectural.noInjection.HandlerNoInjectionRuleProvider;

import javax.annotation.processing.RoundEnvironment;

public class RuleValidatorProvider {
    public static class Behaviour implements RuleValidator{

        @Override
        public void validateRules(RoundEnvironment roundEnv, ProcessorContext ctx) {
            var rules = new BehaviourNoInjectionRuleProvider().provide();
            rules.
                    forEach(s -> s.validate(roundEnv, ctx));
        }
    }
    public static class Handler implements RuleValidator{
        @Override
        public void validateRules(RoundEnvironment roundEnv, ProcessorContext ctx) {
            var rules = new HandlerNoInjectionRuleProvider().provide();
            rules.
                    forEach(s -> s.validate(roundEnv, ctx));
        }
    }
}
