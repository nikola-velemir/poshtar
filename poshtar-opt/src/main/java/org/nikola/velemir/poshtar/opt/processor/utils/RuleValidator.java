package org.nikola.velemir.poshtar.opt.processor.utils;

import org.nikola.velemir.poshtar.opt.rules.Rule;
import org.nikola.velemir.poshtar.opt.rules.RuleContext;
import org.nikola.velemir.poshtar.opt.rules.ambiguity.AmbiguityRule;
import org.nikola.velemir.poshtar.opt.rules.deadPipeline.DeadPipelineRule;
import org.nikola.velemir.poshtar.opt.rules.injection.BehaviourNoInjectionRule;
import org.nikola.velemir.poshtar.opt.rules.injection.HandlerNoInjectionRule;
import org.nikola.velemir.poshtar.opt.rules.requestFinality.RequestFinalityRule;

import javax.annotation.processing.RoundEnvironment;
import java.util.List;

public class RuleValidator {
    private final List<Rule> rules = List.of(
            new RequestFinalityRule(),
            new AmbiguityRule(),
            new HandlerNoInjectionRule(),
            new BehaviourNoInjectionRule(),
            new DeadPipelineRule()
    );

    public void validateRules(RoundEnvironment roundEnv, RuleContext ctx) {
        for (Rule rule : rules) {
            rule.validate(roundEnv, ctx);
        }
    }
}
