package org.nikola.velemir.poshtar.opt.rules.ambiguity;

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
        Map<String, String> seenRequests = new HashMap<>();

        for (var entry : ctx.getRegistry().entrySet()) {
            String handlerFqn = (String) entry.getKey();
            String requestFqn = (String) entry.getValue();
            if ("BEHAVIOUR".equals(requestFqn)) continue;

            if (!seenRequests.containsKey(requestFqn)) {
                seenRequests.put(requestFqn, handlerFqn);
                continue;
            }

            String existingHandler = seenRequests.get(requestFqn);
            if (existingHandler.equals(handlerFqn)) continue;

            logError(ctx, requestFqn, existingHandler, handlerFqn);


        }
    }

    private static void logError(RuleContext ctx, String requestFqn, String existingHandler, String handlerFqn) {
        String errorMessage = String.format("PoshtaR: Ambiguity detected! Request '%s' is handled by both:%n - %s%n - %s",
                requestFqn, existingHandler, handlerFqn);
        Element target = ctx.env.getElementUtils().getTypeElement(handlerFqn);
        ErrorLogger.logError(ctx.env, errorMessage, target);
    }
}