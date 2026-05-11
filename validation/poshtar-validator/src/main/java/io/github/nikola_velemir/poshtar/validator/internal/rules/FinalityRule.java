package io.github.nikola_velemir.poshtar.validator.internal.rules;

import io.github.nikola_velemir.poshtar.validator.internal.context.ProcessorContext;
import io.github.nikola_velemir.poshtar.validator.internal.logger.Logger;
import io.github.nikola_velemir.poshtar.validator.internal.logger.LoggerProvider;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import java.util.Set;

public abstract class FinalityRule implements Rule {

    private static final Logger logger = LoggerProvider.provideErrorLogger();


    protected abstract String getViolationMessage();
    protected abstract Set<String> getFQNs(ProcessorContext ctx);
    /**
     * Validates the finality of request classes. Logs the error if finality is violated.
     * @param roundEnv The environment providing access to elements in the current processing round.
     * @param ctx      Instance of context containing all related request, handler and behavior FQNs.
     */
    @Override
    public void validate(RoundEnvironment roundEnv, ProcessorContext ctx) {
        var fqns = getFQNs(ctx);
        for (var notificationFqn : fqns) {

            TypeElement element = ctx.env.getElementUtils().getTypeElement(notificationFqn);

            boolean isFinalOrRecord = checkIfFinalOrRecord(element);
            if (!isFinalOrRecord) logError(ctx, notificationFqn, element);

        }
    }

    private boolean checkIfFinalOrRecord(TypeElement element) {
        boolean isRecord = element.getKind() == ElementKind.RECORD;
        boolean isFinal = element.getModifiers().contains(Modifier.FINAL);
        return isRecord || isFinal;

    }

    private void logError(ProcessorContext ctx, String requestFqn, TypeElement targetClass) {
        String finalityViolationMessage = getViolationMessage();
        String errorMessage = String.format(finalityViolationMessage, requestFqn);
        logger.log(ctx.env, errorMessage, targetClass);

    }
}