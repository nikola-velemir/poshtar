package org.nikola.velemir.poshtar.opt.internal.rules;

import org.nikola.velemir.poshtar.opt.internal.registry.RegistryEntry;
import org.nikola.velemir.poshtar.opt.internal.logger.ErrorLogger;

import javax.annotation.processing.RoundEnvironment;
import java.util.HashMap;
import java.util.Map;

class AmbiguityRule implements Rule {

    public static final String ALREADY_HANDLED_MESSAGE = "PoshtaR: Ambiguity! Request '%s' is already handled by '%s'";
    public static final String AMBIGUITY_MESSAGE = "PoshtaR: Ambiguity! Request '%s' is also handled by '%s'";

    @Override
    public void validate(RoundEnvironment roundEnv, RuleContext ctx) {
        Map<String, RegistryEntry> seenRequests = new HashMap<>();

        for (var entry : ctx.getHandlerRegistry().values()) {
            String requestFqn = entry.requestFQN();
            if ("BEHAVIOUR".equals(requestFqn)) continue;

            RegistryEntry existing = seenRequests.get(requestFqn);
            if (existing == null) {
                seenRequests.put(requestFqn, entry);
                continue;
            }

            if (existing.handlerFQN().equals(entry.handlerFQN())) continue;

            logError(ctx, requestFqn, existing, entry);

        }
    }

    private static void logError(RuleContext ctx, String requestFqn,
                                 RegistryEntry existing, RegistryEntry conflict) {
        String msgOnConflict = String.format(
                ALREADY_HANDLED_MESSAGE,
                requestFqn, existing.handlerFQN());

        String msgOnExisting = String.format(
                AMBIGUITY_MESSAGE,
                requestFqn, conflict.handlerFQN());

        ErrorLogger.log(ctx.env, msgOnConflict, conflict.handlerElement());
        ErrorLogger.log(ctx.env, msgOnExisting, existing.handlerElement());
    }
}