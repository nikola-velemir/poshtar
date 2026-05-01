package io.github.nikola_velemir.poshtar.opt.internal.rules;

public class RuleValidatorProvider {
    public static RuleValidator provideValidator() {
        return new RuleValidatorImpl();
    }
}
