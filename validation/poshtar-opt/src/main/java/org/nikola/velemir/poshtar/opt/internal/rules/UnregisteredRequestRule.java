package org.nikola.velemir.poshtar.opt.internal.rules;

import org.nikola.velemir.poshtar.opt.api.annotations.request.SuppressUnregistered;
import org.nikola.velemir.poshtar.opt.internal.logger.ErrorLogger;
import org.nikola.velemir.poshtar.opt.processor.ProcessorContext;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.util.Set;

class UnregisteredRequestRule implements Rule {
    private static final ErrorLogger logger = ErrorLogger.getInstance();

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
        return element.getAnnotation(SuppressUnregistered.class) != null;

    }

    private static void logError(ProcessorContext ctx, String requestFqn) {
        String errorMessage = "PoshtaR VIOLATION: No handler registered for request '" + requestFqn + "'\n"
                + "You may use " + SuppressUnregistered.class.getName() + " to bypass this rule!";
        Element target = ctx.env.getElementUtils().getTypeElement(requestFqn);
        logger.log(ctx.env, errorMessage, target);
    }
}
