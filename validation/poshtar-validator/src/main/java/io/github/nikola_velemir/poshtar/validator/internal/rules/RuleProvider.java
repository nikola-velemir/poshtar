package io.github.nikola_velemir.poshtar.validator.internal.rules;

import java.util.List;

/**
 * Provides set of rules of a certain category
 */
public interface RuleProvider {
    /**
     * Valude can be either {@code RuleKind.ARCHITECTURAL} or {@code RuleKind.SEMANTICAL} indicating
     * rule category
     * @return RuleKind of the provider
     */
    RuleKind getKind();

    /**
     * Provides the list of rules for validation
     * @return List of rules to validate.
     */
    List<Rule> provide();
}
