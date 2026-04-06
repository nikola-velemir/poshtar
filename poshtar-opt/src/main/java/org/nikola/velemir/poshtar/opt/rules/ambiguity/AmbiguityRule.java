package org.nikola.velemir.poshtar.opt.rules.ambiguity;

import org.nikola.velemir.poshtar.opt.RegistryEntry;
import org.nikola.velemir.poshtar.opt.processor.utils.ErrorLogger;
import org.nikola.velemir.poshtar.opt.rules.Rule;
import org.nikola.velemir.poshtar.opt.rules.RuleContext;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import java.util.HashMap;
import java.util.Map;

public class AmbiguityRule implements Rule {

    @Override
    public void validate(RoundEnvironment roundEnv, RuleContext ctx) {
        Map<String, RegistryEntry> seenRequests = new HashMap<>();

        for (var entry : ctx.getRegistry().values()) {
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
                "PoshtaR: Ambiguity! Request '%s' is already handled by '%s'",
                requestFqn, existing.handlerFQN());

        String msgOnExisting = String.format(
                "PoshtaR: Ambiguity! Request '%s' is also handled by '%s'",
                requestFqn, conflict.handlerFQN());

        ErrorLogger.logError(ctx.env, msgOnConflict, conflict.handlerElement());
        ErrorLogger.logError(ctx.env, msgOnExisting, existing.handlerElement());
    }
}