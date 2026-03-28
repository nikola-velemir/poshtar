package org.nikola.velemir.poshtar.opt.rules;

import javax.annotation.processing.RoundEnvironment;

public interface Rule {

    void validate(RoundEnvironment roundEnv, RuleContext ctx);

}