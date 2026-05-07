package io.github.nikola_velemir.poshtar.opt.internal.rules;

/**
 * Class acts as an injector to provide {@link RuleValidator} implementation, since implementation is package private.
 */
public class RuleValidatorProvider {
    /**
     * Provides the implementation of {@link RuleValidator}, as the concrete implementation is package-private.
     *
     * @return Implementation of the {@link RuleValidator}, which will be used by the processor.
     */
    public static RuleValidator provideValidator() {
        return new RuleValidatorImpl();
    }
}
