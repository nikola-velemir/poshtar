package io.github.nikola_velemir.poshtar.opt.internal.rules;

import io.github.nikola_velemir.poshtar.opt.internal.context.ProcessorContext;

import javax.annotation.processing.RoundEnvironment;

/**
 * Interface defines a rule that models a validation logic.
 *
 * <p>
 * Implementations of this interface are responsible for inspecting the source code
 * during compilation to ensure that Poshtar components (Handlers, Requests, and Behaviors)
 * adhere to the library's required structure and design patterns.
 * </p>
 *
 * @author Nikola Velemir
 * @version ${project.version}
 * @since 1.0.0
 */
public interface Rule {
    /**
     * Executes the validation logic for this specific rule.
     * If a violation is found, logic should dispatch an error or a warning message to a compiler, depending on its severity.
     *
     * @param roundEnv The environment providing access to elements in the current processing round.
     * @param ctx      Instance of context containing all related request, handler and behavior FQNs.
     */
    void validate(RoundEnvironment roundEnv, ProcessorContext ctx);

}