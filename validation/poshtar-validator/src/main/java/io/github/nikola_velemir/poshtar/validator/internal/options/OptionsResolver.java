package io.github.nikola_velemir.poshtar.validator.internal.options;

import io.github.nikola_velemir.poshtar.validator.internal.rules.RuleKind;
import io.github.nikola_velemir.poshtar.validator.processor.ProcessorConstants;

import javax.annotation.processing.ProcessingEnvironment;
import javax.tools.Diagnostic;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.Locale;
import java.util.Set;

public class OptionsResolver {
    public static Set<RuleKind> resolveEnabledRuleKinds(ProcessingEnvironment env) {
        String opt = env.getOptions().get(ProcessorConstants.OPTIONS.get(ProcessorConstants.OptionKey.RULES));

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
