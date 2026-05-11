package io.github.nikola_velemir.poshtar.validator.internal.rules;

import io.github.nikola_velemir.poshtar.validator.internal.context.ProcessorContext;
import io.github.nikola_velemir.poshtar.validator.internal.logger.Logger;
import io.github.nikola_velemir.poshtar.validator.internal.logger.LoggerProvider;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;

public class NotificationFinalityRule implements Rule {

    private static final String FINALITY_VIOLATED_MESSAGE = "PoshtaR: Finality Violated! Notification '%s' must be final or a record!";
    private static final Logger logger = LoggerProvider.provideErrorLogger();

    /**
     * Validates the finality of request classes. Logs the error if finality is violated.
     * @param roundEnv The environment providing access to elements in the current processing round.
     * @param ctx      Instance of context containing all related request, handler and behavior FQNs.
     */
    @Override
    public void validate(RoundEnvironment roundEnv, ProcessorContext ctx) {

        for (var notificationFqn : ctx.getKnownNotifications()) {

            TypeElement element = ctx.env.getElementUtils().getTypeElement(notificationFqn);

            boolean isFinalOrRecord = checkIfFinalOrRecord(element);
            if (!isFinalOrRecord) logError(ctx, notificationFqn, element);

        }
    }

    private static boolean checkIfFinalOrRecord(TypeElement element) {
        boolean isRecord = element.getKind() == ElementKind.RECORD;
        boolean isFinal = element.getModifiers().contains(Modifier.FINAL);
        return isRecord || isFinal;

    }

    private static void logError(ProcessorContext ctx, String requestFqn, TypeElement targetClass) {
        String errorMessage = String.format(FINALITY_VIOLATED_MESSAGE, requestFqn);
        logger.log(ctx.env, errorMessage, targetClass);

    }
}
