package org.nikola.velemir.poshtar.opt.processor.utils;

import org.nikola.velemir.poshtar.opt.rules.Rule;
import org.nikola.velemir.poshtar.opt.rules.RuleContext;
import org.nikola.velemir.poshtar.opt.rules.RuleProvider;


import javax.annotation.processing.RoundEnvironment;
import java.util.List;

public class RuleValidator {
    private final List<Rule> rules = RuleProvider.provideRules();

    public void validateRules(RoundEnvironment roundEnv, RuleContext ctx) {
        for (Rule rule : rules) {
            rule.validate(roundEnv, ctx);
        }
    }
}
