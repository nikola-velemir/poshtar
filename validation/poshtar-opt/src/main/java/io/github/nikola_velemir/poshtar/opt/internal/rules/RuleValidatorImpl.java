package io.github.nikola_velemir.poshtar.opt.internal.rules;


import io.github.nikola_velemir.poshtar.opt.internal.context.ProcessorContext;

import javax.annotation.processing.RoundEnvironment;
import java.util.List;

/**
 * Base implementation of the {@link RuleValidator}.
 * <p>During validation, passes thru the list of rules provided by {@link RuleProvider}, calling validate on each one</p>
 *
 */
class RuleValidatorImpl implements RuleValidator {
    private final List<Rule> rules = RuleProvider.provideRules();

    public void validateRules(RoundEnvironment roundEnv, ProcessorContext ctx) {
        for (Rule rule : rules) {
            rule.validate(roundEnv, ctx);
        }
    }
}
