package io.github.nikola_velemir.poshtar.opt.internal.rules;

import io.github.nikola_velemir.poshtar.opt.internal.context.ProcessorContext;

import javax.annotation.processing.RoundEnvironment;

/**
 * Interface defines a validator of the architectural rules, specified by the library.
 */
public interface RuleValidator {
    /**
     * Method validates all rules provided to the implementation of this interface.
     *
     * @param roundEnv The environment providing access to elements in the current processing round.
     * @param ctx      Instance of context containing all.
     */
    void validateRules(RoundEnvironment roundEnv, ProcessorContext ctx);
}
