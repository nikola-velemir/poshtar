package org.nikola.velemir.poshtar.opt.internal.rules;

import org.nikola.velemir.poshtar.opt.internal.context.ProcessorContext;

import javax.annotation.processing.RoundEnvironment;

public interface RuleValidator {
    void validateRules(RoundEnvironment roundEnv, ProcessorContext ctx);
}
