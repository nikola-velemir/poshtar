package io.github.nikola_velemir.poshtar.validator.internal.options;

import io.github.nikola_velemir.poshtar.validator.internal.rules.RuleKind;

import javax.annotation.processing.ProcessingEnvironment;
import javax.tools.Diagnostic;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.Locale;
import java.util.Set;

/**
 * Resolves options, regarding which category of rules should be employed when validated the source code.
 */
public class OptionsResolver {
    /**
     * Resolves the enabled rule kinds, set thru xml config.
     * @param optionName Name of the option in xml config.
     * @param env Build environment
     * @return Set of all enabled rule kinds.
     */
    public static Set<RuleKind> resolveEnabledRuleKinds(String optionName,ProcessingEnvironment env) {
        String opt = env.getOptions().get(optionName);

        if (opt == null || opt.isBlank()) {
            return EnumSet.allOf(RuleKind.class);
        }

        Set<RuleKind> kinds = EnumSet.noneOf(RuleKind.class);
        for (String token : opt.split(",")) {
            String trimmed = token.trim();
            if (trimmed.isEmpty()) {
                continue;
            }
            try {
                kinds.add(RuleKind.valueOf(trimmed.toUpperCase(Locale.ROOT)));
            } catch (IllegalArgumentException e) {
                env.getMessager().printMessage(
                        Diagnostic.Kind.ERROR,
                        "Unknown Poshtar rule kind '" + trimmed + "'. Valid values: " + Arrays.toString(RuleKind.values())
                );
            }
        }
        return kinds;
    }
}
