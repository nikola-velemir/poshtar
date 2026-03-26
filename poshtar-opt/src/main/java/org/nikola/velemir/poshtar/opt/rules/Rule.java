package org.nikola.velemir.poshtar.opt.rules;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.TypeElement;

public interface Rule {
    void validate(TypeElement element, RuleContext ctx);

    void validateRound(RoundEnvironment roundEnv, RuleContext ctx);

}