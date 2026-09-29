package io.github.nikola_velemir.poshtar.validator.internal.rules.semantical.intent.handler.implementation;

import io.github.nikola_velemir.poshtar.core.request.handler.CommandHandler;
import io.github.nikola_velemir.poshtar.core.request.handler.QueryHandler;
import io.github.nikola_velemir.poshtar.core.request.handler.RequestHandler;
import io.github.nikola_velemir.poshtar.core.request.handler.VoidCommandHandler;
import io.github.nikola_velemir.poshtar.validator.internal.context.ProcessorContext;
import io.github.nikola_velemir.poshtar.validator.internal.logger.Logger;
import io.github.nikola_velemir.poshtar.validator.internal.logger.LoggerProvider;
import io.github.nikola_velemir.poshtar.validator.internal.rules.Rule;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;

class RequestHandlerImplementationRule implements Rule {
    private static final String REQUEST_HANDLER_FQN = RequestHandler.class.getName();
    private static final Logger logger = LoggerProvider.provideWarningLogger();
    private static final String WARNING_MESSAGE = String.format("Using %s is discouraged, to better declare semantical intent use either %s, %s or %s",
            RequestHandler.class.getName(), QueryHandler.class.getName(), CommandHandler.class.getName(), VoidCommandHandler.class.getName());
    @Override
    public void validate(RoundEnvironment roundEnv, ProcessorContext ctx) {
        var elements = ctx.getElements();
        var types = ctx.getTypes();

        var handlerType = elements.getTypeElement(REQUEST_HANDLER_FQN);
        if (handlerType == null) return;

        var handlerErasure = types.erasure(handlerType.asType());
        var handlers = ctx.getRequestHandlerFQNS();

        for (var handlerFqn : handlers) {
            TypeElement handler = elements.getTypeElement(handlerFqn);
            if(handler == null) continue;

            for (TypeMirror mirror : handler.getInterfaces()){

                var mirrorErasure = types.erasure(mirror);
                if(!types.isSameType(mirrorErasure, handlerErasure)) continue;
                logger.log(ctx.env, WARNING_MESSAGE, handler);
            }
        }


    }
}
