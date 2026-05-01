package io.github.nikola_velemir.poshtar.opt.internal.rules;

import io.github.nikola_velemir.poshtar.core.pipeline.behaviour.PipelineBehaviour;
import io.github.nikola_velemir.poshtar.core.request.handler.RequestHandler;
import io.github.nikola_velemir.poshtar.opt.internal.logger.Logger;
import io.github.nikola_velemir.poshtar.opt.internal.logger.LoggerProvider;
import io.github.nikola_velemir.poshtar.opt.internal.context.ProcessorContext;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;

class SingleResponsibilityHandlerRule implements Rule {

    private static final String VIOLATION_MESSAGE = "PoshtaR VIOLATION: A class implementing %s or %s may only implement one of given interfaces.";
    private static final Logger logger = LoggerProvider.provideErrorLogger();

    @Override
    public void validate(RoundEnvironment roundEnv, ProcessorContext ctx) {
        var entries = ctx.getHandlerRegistry();
        for (var entry : entries.values()) {
            var handlerElement = (TypeElement) entry.handlerElement();

            boolean implementsHandler = checkIfImplementsHandler(ctx, handlerElement);
            boolean implementsBehaviour = checkIfImplementsBehaviour(ctx, handlerElement);

            if (!implementsHandler || !implementsBehaviour) continue;

            logError(ctx, handlerElement);

        }
    }

    private static void logError(ProcessorContext ctx, TypeElement handlerElement) {

        String errorMessage = String.format(
                VIOLATION_MESSAGE,
                RequestHandler.class.getName(),
                PipelineBehaviour.class.getName()
        );

        logger.log(ctx.env, errorMessage, handlerElement);
    }

    private static boolean checkIfImplementsHandler(ProcessorContext ctx, TypeElement handlerElement) {
        return handlerElement
                .getInterfaces()
                .stream()
                .anyMatch(t -> checkIfType(ctx, t, RequestHandler.class.getName()));
    }

    private static boolean checkIfImplementsBehaviour(ProcessorContext ctx, TypeElement handlerElement) {
        return handlerElement
                .getInterfaces()
                .stream()
                .anyMatch(t -> checkIfType(ctx, t, PipelineBehaviour.class.getName()));
    }

    private static boolean checkIfType(ProcessorContext ctx, TypeMirror iface, String interfaceFqn) {
        TypeElement targetElement = ctx.getElements().getTypeElement(interfaceFqn);
        if (targetElement == null) return false;
        TypeMirror targetType = ctx.getTypes().erasure(targetElement.asType());
        TypeMirror implementationErasure = ctx.env.getTypeUtils().erasure(iface);

        return ctx.getTypes().isAssignable(implementationErasure, targetType);
    }
}
