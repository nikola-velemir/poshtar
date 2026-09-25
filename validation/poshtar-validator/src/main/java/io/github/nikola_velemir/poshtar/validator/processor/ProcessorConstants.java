package io.github.nikola_velemir.poshtar.validator.processor;

import io.github.nikola_velemir.poshtar.core.annotations.Behaviour;
import io.github.nikola_velemir.poshtar.core.annotations.Handler;

import java.util.Map;

/**
 * Class containing all constants required by the main {@link PoshtarValidationProcessor}.
 */
class ProcessorConstants {
        /**
         * Supported annotations
         */
    public static final String[] ANNOTATIONS = {
            Handler.class.getName(),
            Behaviour.class.getName()
    };

    /**
     * Enumeration represetning the option key, contained in the config of the application.
     * OptionKey
     */
    public enum OptionKey {
        /**Depicts rule key */
        RULES,
        /**Depicts trictness key */
        STRICT
    }

    /**
     * Option map
     */
    public static final Map<OptionKey, String> OPTIONS = Map.of(
            OptionKey.RULES, "poshtar.rules",
            OptionKey.STRICT, "poshtar.strict"
    );
 }
