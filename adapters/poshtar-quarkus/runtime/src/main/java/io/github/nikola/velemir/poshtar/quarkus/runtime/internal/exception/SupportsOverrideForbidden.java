package io.github.nikola.velemir.poshtar.quarkus.runtime.internal.exception;

import io.github.nikola_velemir.poshtar.core.exceptions.PoshtarException;

/**
 * Thrown if an invalid attempt is made to evaluate request compatibility
 * or pipeline behavior eligibility at runtime.
 *
 * <p>
 * To maximize optimization and maintain zero-reflection execution paths, the PoshtaR Quarkus extension
 * performs all pipeline behavior routing calculations statically during the compilation and deployment phase.
 * Overriding or triggering runtime behavior checks violates this core architectural invariant.
 * </p>
 *
 * @author Nikola Velemir
 * @since 1.0.0
 */
public class SupportsOverrideForbidden extends PoshtarException {
    /**
     * Constructs a new exception instance with a customized detail message.
     *
     * @param message the detail message explaining the cause of the failure
     */
    public SupportsOverrideForbidden(String message) {
        super(message);
    }

    /**
     * Constructs a default exception instance indicating that execution path evaluation
     * is explicitly forbidden at runtime.
     */
    public SupportsOverrideForbidden() {
        super("supportsRequest should never be called at runtime");
    }
}
