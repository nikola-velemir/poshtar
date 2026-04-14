package org.nikola.velemir.poshtar.opt.internal.rules;

import org.nikola.velemir.poshtar.opt.processor.ProcessorContext;

import javax.annotation.processing.RoundEnvironment;

public interface RuleValidator {
    void validateRules(RoundEnvironment roundEnv, ProcessorContext ctx);

    static RuleValidator provideImpl() {
        return new RuleValidatorImpl();
    }
}
