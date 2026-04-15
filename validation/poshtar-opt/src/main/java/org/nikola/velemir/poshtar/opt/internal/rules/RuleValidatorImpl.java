package org.nikola.velemir.poshtar.opt.internal.rules;


import org.nikola.velemir.poshtar.opt.internal.context.ProcessorContext;

import javax.annotation.processing.RoundEnvironment;
import java.util.List;

class RuleValidatorImpl implements RuleValidator {
    private final List<Rule> rules = RuleProvider.provideRules();

    public void validateRules(RoundEnvironment roundEnv, ProcessorContext ctx) {
        for (Rule rule : rules) {
            rule.validate(roundEnv, ctx);
        }
    }
}
