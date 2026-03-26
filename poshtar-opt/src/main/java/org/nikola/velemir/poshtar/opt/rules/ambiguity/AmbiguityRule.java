package org.nikola.velemir.poshtar.opt.rules.ambiguity;

import org.nikola.velemir.poshtar.core.annotations.Handler;
import org.nikola.velemir.poshtar.core.request.handler.RequestHandler;
import org.nikola.velemir.poshtar.opt.rules.Rule;
import org.nikola.velemir.poshtar.opt.rules.RuleContext;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic;
import java.util.HashMap;
import java.util.List;
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
        ctx.env.getMessager().printMessage(
                Diagnostic.Kind.ERROR,
                String.format("PoshtaR: Ambiguity detected! Request '%s' is handled by both:%n - %s%n - %s",
                        requestFqn, existingHandler, handlerFqn)
        );
    }
}