package org.nikola.velemir.poshtar.opt.internal.rules;

import org.nikola.velemir.poshtar.opt.internal.logger.ErrorLogger;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;

class RequestFinalityRule implements Rule {

    public static final String FINALITY_VIOLATED_MESSAGE = "PoshtaR: Finality Violated! Request '%s' must be final or a record!";

    @Override
    public void validate(RoundEnvironment roundEnv, RuleContext ctx) {

        for (var requestFqn : ctx.getKnownRequests()) {
            if ("BEHAVIOUR".equals(requestFqn)) continue;

            TypeElement element = ctx.env.getElementUtils().getTypeElement(requestFqn);

            boolean isFinalOrRecord = checkIfFinalOrRecord(element);
            if (!isFinalOrRecord) logError(ctx, requestFqn, element);

        }
    }
    private static boolean checkIfFinalOrRecord(TypeElement element){
        boolean isRecord = element.getKind() == ElementKind.RECORD;
        boolean isFinal = element.getModifiers().contains(Modifier.FINAL);
        return isRecord || isFinal;

    }
    private static void logError(RuleContext ctx, String requestFqn, TypeElement targetClass) {
        String errorMessage = String.format(FINALITY_VIOLATED_MESSAGE, requestFqn);
        ErrorLogger.log(ctx.env, errorMessage, targetClass);

    }
}
