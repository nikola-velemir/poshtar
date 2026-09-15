package io.github.nikola_velemir.poshtar.validator.processor;

import io.github.nikola_velemir.poshtar.core.annotations.Behaviour;
import io.github.nikola_velemir.poshtar.core.annotations.Handler;

import java.util.Map;

class ProcessorConstants {
    public static final String[] ANNOTATIONS = {
            Handler.class.getName(),
            Behaviour.class.getName()
    };

    public enum OptionKey {
        RULES,
        STRICT
    }

    public static final Map<OptionKey, String> OPTIONS = Map.of(
            OptionKey.RULES, "poshtar.rules",
            OptionKey.STRICT, "poshtar.strict"
    );
 }
