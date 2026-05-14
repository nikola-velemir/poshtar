package io.github.nikola_velemir.poshtar.validator.internal.rules.deadPipeline;

import io.github.nikola_velemir.poshtar.validator.internal.rules.Rule;

import java.util.List;

public class DeadPipelineRuleProvider {
    public static List<Rule> provide() {
        return List.of(new DeadPipelineRule());
    }
}
