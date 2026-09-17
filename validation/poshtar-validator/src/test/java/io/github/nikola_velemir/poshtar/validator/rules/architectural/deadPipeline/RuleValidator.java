package io.github.nikola_velemir.poshtar.validator.rules.architectural.deadPipeline;

import io.github.nikola_velemir.poshtar.validator.internal.context.ProcessorContext;
import io.github.nikola_velemir.poshtar.validator.internal.rules.Rule;
import io.github.nikola_velemir.poshtar.validator.internal.rules.architectural.deadPipeline.DeadPipelineRuleProvider;

import javax.annotation.processing.RoundEnvironment;
import java.util.List;

class RuleValidator implements io.github.nikola_velemir.poshtar.validator.internal.rules.RuleValidator {
    @Override
    public void validateRules(RoundEnvironment roundEnv, ProcessorContext ctx) {
        List<Rule> rules = (new DeadPipelineRuleProvider()).provide();
        rules
                .forEach(s -> s.validate(roundEnv, ctx));

    }
}
