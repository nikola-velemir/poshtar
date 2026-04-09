package org.nikola.velemir.poshtar.opt.rules;

import org.nikola.velemir.poshtar.core.pipeline.behaviour.PipelineBehaviour;
import org.nikola.velemir.poshtar.core.request.handler.RequestHandler;
import org.nikola.velemir.poshtar.opt.utils.logger.ErrorLogger;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;

class SingleResponsibilityHandlerRule implements Rule {

    public static final String VIOLATION_MESSAGE = "PoshtaR VIOLATION: A class implementing %s or %s may only implement one of given interfaces.";

    @Override
    public void validate(RoundEnvironment roundEnv, RuleContext ctx) {
        var entries = ctx.getHandlerRegistry();
        for (var entry : entries.values()) {
            var handlerElement = (TypeElement) entry.handlerElement();

            boolean implementsHandler = checkIfImplementsHandler(ctx, handlerElement);
            boolean implementsBehaviour = checkIfImplementsBehaviour(ctx, handlerElement);

            if (!implementsHandler || !implementsBehaviour) continue;

            logError(ctx, handlerElement);

        }
    }

    private static void logError(RuleContext ctx, TypeElement handlerElement) {

        String errorMessage = String.format(
                VIOLATION_MESSAGE,
                RequestHandler.class.getName(),
                PipelineBehaviour.class.getName()
        );

        ErrorLogger.log(ctx.env, errorMessage, handlerElement);
    }

    private static boolean checkIfImplementsHandler(RuleContext ctx, TypeElement handlerElement) {
        return handlerElement
                .getInterfaces()
                .stream()
                .anyMatch(t -> checkIfType(ctx, t, RequestHandler.class.getName()));
    }

    private static boolean checkIfImplementsBehaviour(RuleContext ctx, TypeElement handlerElement) {
        return handlerElement
                .getInterfaces()
                .stream()
                .anyMatch(t -> checkIfType(ctx, t, PipelineBehaviour.class.getName()));
    }

    private static boolean checkIfType(RuleContext ctx, TypeMirror iface, String interfaceFqn) {
        TypeElement targetElement = ctx.getElements().getTypeElement(interfaceFqn);
        if (targetElement == null) return false;
        TypeMirror targetType = ctx.getTypes().erasure(targetElement.asType());
        TypeMirror implementationErasure = ctx.env.getTypeUtils().erasure(iface);

        return ctx.getTypes().isAssignable(implementationErasure, targetType);
    }
}
