package io.github.nikola_velemir.poshtar.validator.internal.rules.semantical.intent.request;

import io.github.nikola_velemir.poshtar.core.request.Command;
import io.github.nikola_velemir.poshtar.core.request.Query;
import io.github.nikola_velemir.poshtar.core.request.Request;
import io.github.nikola_velemir.poshtar.core.request.VoidCommand;
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

public class RequestIntentRule implements Rule {
    private static final String REQUEST_FQN = Request.class.getName();
    private static final Logger logger = LoggerProvider.provideWarningLogger();
    private static final String WARNING_MESSAGE = String.format("Using %s is discouraged, to better declare semantical intent use either %s, %s or %s",
            Request.class.getName(), Query.class.getName(), Command.class.getName(), VoidCommand.class.getName());
    @Override
    public void validate(RoundEnvironment roundEnv, ProcessorContext ctx) {
        var elements = ctx.getElements();
        var types = ctx.getTypes();

        var requestType = elements.getTypeElement(REQUEST_FQN);
        if (requestType == null) return;

        var requestErasure = types.erasure(requestType.asType());
        var requests = ctx.getKnownRequests();

        for (var requestFqn : requests) {
            TypeElement request = elements.getTypeElement(requestFqn);
            if(request == null) continue;

            for (TypeMirror mirror : request.getInterfaces()){

                var mirrorErasure = types.erasure(mirror);
                if(!types.isSameType(mirrorErasure, requestErasure)) continue;
                logger.log(ctx.env, WARNING_MESSAGE, request);
            }
        }


    }
}

