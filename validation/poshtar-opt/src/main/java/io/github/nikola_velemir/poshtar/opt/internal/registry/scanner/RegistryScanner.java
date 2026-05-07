package io.github.nikola_velemir.poshtar.opt.internal.registry.scanner;

import io.github.nikola_velemir.poshtar.opt.internal.context.ProcessorContext;

import javax.annotation.processing.RoundEnvironment;

/**
 * Interface that models a scanner, which is to perform class path scanning
 * to related components to later use the for validating.
 *
 * @author Nikola Velemir
 * @version ${project.version}
 * @since 1.0.0
 */
public interface RegistryScanner {
    /**
     * Override is to control the scanning process for the current processing round.
     *
     * @param roundEnv The environment representing the current round of annotation processing.
     * @param ctx      The shared context where discovered components are stored.
     */
    void scanRegistry(RoundEnvironment roundEnv, ProcessorContext ctx);
}
