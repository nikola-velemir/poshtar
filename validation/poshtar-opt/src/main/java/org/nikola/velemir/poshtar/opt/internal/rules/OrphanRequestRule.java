package org.nikola.velemir.poshtar.opt.internal.rules;

import org.nikola.velemir.poshtar.opt.api.annotations.request.SuppressOrphan;
import org.nikola.velemir.poshtar.opt.internal.logger.ErrorLogger;
import org.nikola.velemir.poshtar.opt.processor.ProcessorContext;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.util.Set;

class OrphanRequestRule implements Rule {
    private static final ErrorLogger logger = ErrorLogger.getInstance();
    private static final String VIOLATION_MESSAGE = "PoshtaR VIOLATION: No handler registered for request '%s'\n" + "You may use %s to bypass this rule!";

    @Override
    public void validate(RoundEnvironment roundEnv, ProcessorContext ctx) {
        Set<String> handledTypes = ctx.getHandledRequestTypes();

        for (var requestFqn : ctx.getKnownRequests()) {
            if (handledTypes.contains(requestFqn)) continue;
            TypeElement element = ctx.env.getElementUtils().getTypeElement(requestFqn);
            if (element == null) continue;
            if (hasSuppression(element)) continue;

            logError(ctx, requestFqn);

        }
    }

    private boolean hasSuppression(TypeElement element) {
        return element.getAnnotation(SuppressOrphan.class) != null;

    }

    private static void logError(ProcessorContext ctx, String requestFqn) {
        String errorMessage = String.format(VIOLATION_MESSAGE, requestFqn, SuppressOrphan.class.getCanonicalName());
        Element target = ctx.env.getElementUtils().getTypeElement(requestFqn);
        logger.log(ctx.env, errorMessage, target);
    }
}
