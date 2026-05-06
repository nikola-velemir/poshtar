package io.github.nikola_velemir.poshtar.opt.internal.rules;

/**
 * Class acts as an injector to provide {@link RuleValidator} implementation, since implementation is package private.
 */
public class RuleValidatorProvider {
    public static RuleValidator provideValidator() {
        return new RuleValidatorImpl();
    }
}
