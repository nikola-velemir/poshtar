package org.nikola.velemir.poshtar.opt.rules;

import org.nikola.velemir.poshtar.opt.processor.utils.ErrorLogger;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;

class RequestFinalityRule implements Rule {
    @Override
    public void validate(RoundEnvironment roundEnv, RuleContext ctx) {

        for (var entry : ctx.getRegistry().entrySet()) {
            String requestFqn = entry.getValue().requestFQN();
            if ("BEHAVIOUR".equals(requestFqn)) continue;

            TypeElement element = ctx.env.getElementUtils().getTypeElement(requestFqn);

            boolean isRecord = element.getKind() == ElementKind.RECORD;
            boolean isFinal = element.getModifiers().contains(Modifier.FINAL);

            if (!isRecord && !isFinal) logError(ctx, requestFqn);

        }
    }

    private static void logError(RuleContext ctx, String requestFqn) {
        String errorMessage = String.format("PoshtaR: Finality Violated! Request '%s' must be final or a record!",
                requestFqn);
        ErrorLogger.logError(ctx.env, errorMessage);

    }
}
