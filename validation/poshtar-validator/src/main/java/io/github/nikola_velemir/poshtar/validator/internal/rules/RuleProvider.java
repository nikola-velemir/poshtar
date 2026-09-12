package io.github.nikola_velemir.poshtar.validator.internal.rules;

import java.util.List;

public interface RuleProvider {
    RuleKind getKind();
    List<Rule> provide();
}
