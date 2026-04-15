package org.nikola.velemir.poshtar.opt.internal.registry.scanner;

import org.nikola.velemir.poshtar.opt.processor.ProcessorContext;

import javax.annotation.processing.RoundEnvironment;

public interface RegistryScanner {
    void scanRegistry(RoundEnvironment roundEnv, ProcessorContext ctx);
}
