package org.nikola.velemir.poshtar.opt.internal.rules;

import org.nikola.velemir.poshtar.opt.processor.ProcessorContext;

import javax.annotation.processing.RoundEnvironment;

public interface Rule {

    void validate(RoundEnvironment roundEnv, ProcessorContext ctx);

}