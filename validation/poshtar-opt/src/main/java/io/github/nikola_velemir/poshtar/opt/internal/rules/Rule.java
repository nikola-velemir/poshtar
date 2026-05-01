package io.github.nikola_velemir.poshtar.opt.internal.rules;

import io.github.nikola_velemir.poshtar.opt.internal.context.ProcessorContext;

import javax.annotation.processing.RoundEnvironment;

public interface Rule {

    void validate(RoundEnvironment roundEnv, ProcessorContext ctx);

}