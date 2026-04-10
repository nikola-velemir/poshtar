package org.nikola.velemir.poshtar.opt.internal.rules;

import javax.annotation.processing.RoundEnvironment;

public interface Rule {

    void validate(RoundEnvironment roundEnv, RuleContext ctx);

}