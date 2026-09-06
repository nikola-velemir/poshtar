package io.github.nikola_velemir.poshtar.validator.internal.rules;

import java.util.List;

public interface RuleProvider {
    static List<Rule> provide() {
        return List.of();
    }
}
