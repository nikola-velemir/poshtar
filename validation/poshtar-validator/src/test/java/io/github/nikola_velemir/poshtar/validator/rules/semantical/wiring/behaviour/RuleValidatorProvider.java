package io.github.nikola_velemir.poshtar.validator.rules.semantical.wiring.behaviour;

import io.github.nikola_velemir.poshtar.validator.internal.context.ProcessorContext;
import io.github.nikola_velemir.poshtar.validator.internal.rules.RuleValidator;
import io.github.nikola_velemir.poshtar.validator.internal.rules.semantical.wiring.behaviour.BehaviourWiringRuleProvider;

import javax.annotation.processing.RoundEnvironment;

class RuleValidatorProvider implements RuleValidator {
    @Override
    public void validateRules(RoundEnvironment roundEnv, ProcessorContext ctx) {
        var rules = new BehaviourWiringRuleProvider().provide();
        rules.
                forEach(s -> s.validate(roundEnv, ctx));
    }
}

