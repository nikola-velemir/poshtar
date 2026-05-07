package io.github.nikola_velemir.poshtar.opt.internal.rules;

import io.github.nikola_velemir.poshtar.opt.internal.logger.Logger;
import io.github.nikola_velemir.poshtar.opt.internal.logger.LoggerProvider;
import io.github.nikola_velemir.poshtar.opt.internal.registry.RegistryEntry;
import io.github.nikola_velemir.poshtar.opt.internal.context.ProcessorContext;

import javax.annotation.processing.RoundEnvironment;
import java.util.HashMap;
import java.util.Map;

/**
 * Architectural rule that enforces a strict one-to-one mapping between a Request and its Handler.
 * <p>
 * This rule inspects the discovered handler registry to ensure that no request type
 * is associated with multiple handler implementations. If a conflict is found, it reports
 * an error on both conflicting handler classes, pinpointing the ambiguity.
 * </p>
 *
 * @author Nikola Velemir
 * @version ${project.version}
 * @see io.github.nikola_velemir.poshtar.core.exceptions.AmbiguousHandlerException
 * @since 1.0.0
 */
class AmbiguityRule implements Rule {

    public static final String ALREADY_HANDLED_MESSAGE = "PoshtaR: Ambiguity! Request '%s' is already handled by '%s'";
    public static final String AMBIGUITY_MESSAGE = "PoshtaR: Ambiguity! Request '%s' is also handled by '%s'";
    private static final Logger logger = LoggerProvider.provideErrorLogger();

    /**
     * Performs the ambiguity check against the current handler registry.
     *
     * @param roundEnv The current annotation processing round environment.
     * @param ctx      Instance of context containing all related request, handler and behavior FQNs.
     */
    @Override
    public void validate(RoundEnvironment roundEnv, ProcessorContext ctx) {
        Map<String, RegistryEntry> seenRequests = new HashMap<>();

        for (var entry : ctx.getHandlerRegistry().values()) {
            String requestFqn = entry.requestFQN();
            if ("BEHAVIOUR".equals(requestFqn)) continue;

            var existing = seenRequests.get(requestFqn);
            if (existing == null) {
                seenRequests.put(requestFqn, entry);
                continue;
            }

            if (existing.handlerFQN().equals(entry.handlerFQN())) continue;

            logError(ctx, requestFqn, existing, entry);

        }
    }

    /**
     * Dispatches error messages to the compiler for both conflicting elements.
     *
     * @param ctx      Instance of context containing all related request, handler and behavior FQNs.     * @param requestFqn The name of the request causing the conflict.
     * @param existing The handler entry previously found.
     * @param conflict The new handler entry causing the ambiguity.
     */
    private static void logError(ProcessorContext ctx, String requestFqn,
                                 RegistryEntry existing, RegistryEntry conflict) {
        String msgOnConflict = String.format(
                ALREADY_HANDLED_MESSAGE,
                requestFqn, existing.handlerFQN());

        String msgOnExisting = String.format(
                AMBIGUITY_MESSAGE,
                requestFqn, conflict.handlerFQN());

        logger.log(ctx.env, msgOnConflict, conflict.handlerElement());
        logger.log(ctx.env, msgOnExisting, existing.handlerElement());
    }
}