package io.github.nikola_velemir.poshtar.validator.internal.rules.registration;

import io.github.nikola_velemir.poshtar.validator.internal.rules.Rule;

import java.util.List;

public class RegistrationRuleProvider {
    public static List<Rule> provide() {
        return List.of(
                new AmbiguityRule(),
                new OrphanRequestRule()
        );
    }
}
