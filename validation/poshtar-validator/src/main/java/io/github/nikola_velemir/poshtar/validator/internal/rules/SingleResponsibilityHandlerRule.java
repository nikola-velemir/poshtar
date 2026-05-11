/*
 * Copyright 2026 Nikola Velemir
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.nikola_velemir.poshtar.validator.internal.rules;

import io.github.nikola_velemir.poshtar.core.notification.handler.NotificationHandler;
import io.github.nikola_velemir.poshtar.core.pipeline.behaviour.PipelineBehaviour;
import io.github.nikola_velemir.poshtar.core.request.handler.RequestHandler;
import io.github.nikola_velemir.poshtar.validator.internal.logger.Logger;
import io.github.nikola_velemir.poshtar.validator.internal.logger.LoggerProvider;
import io.github.nikola_velemir.poshtar.validator.internal.context.ProcessorContext;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;

/**
 * Rule that prevents the developer from declaring a class both as a handler and a behavior.
 *
 * <p>
 * Class can have only on responsibility. In context of this library,
 * class that is to handle a request may only be either a handler or a behavior.
 * Rule prevents from implementing two of  {@link RequestHandler}, {@link NotificationHandler} and {@link PipelineBehaviour}.
 * </p>
 *
 * @author Nikola Velemir
 * @version ${project.version}
 * @since 1.0.0
 */
class SingleResponsibilityHandlerRule implements Rule {

    private static final String VIOLATION_MESSAGE = "PoshtaR VIOLATION: A class implementing %s or %s may only implement one of given interfaces.";
    private static final Logger logger = LoggerProvider.provideErrorLogger();

    /**
     * Checks if class in question implements two of {@link NotificationHandler}, {@link RequestHandler} and {@link PipelineBehaviour}
     *
     * @param roundEnv The environment providing access to elements in the current processing round.
     * @param ctx      Instance of context containing all related request, handler and behavior FQNs.
     */
    @Override
    public void validate(RoundEnvironment roundEnv, ProcessorContext ctx) {
        var entries = ctx.getHandlerRegistry();
        for (var entry : entries.values()) {
            var handlerElement = (TypeElement) entry.handlerElement();

            int interfaceCount = 0;
            if (checkIfImplementsRequestHandler(ctx, handlerElement)) ++interfaceCount;
            if (checkIfImplementsNotificationHandler(ctx, handlerElement)) ++interfaceCount;
            if (checkIfImplementsBehaviour(ctx, handlerElement)) ++interfaceCount;

            System.out.println(interfaceCount);
            if (interfaceCount <= 1) continue;

            logError(ctx, handlerElement);

        }
    }

    private static void logError(ProcessorContext ctx, TypeElement handlerElement) {

        String errorMessage = String.format(
                VIOLATION_MESSAGE,
                RequestHandler.class.getName(),
                NotificationHandler.class.getName(),
                PipelineBehaviour.class.getName()
        );

        logger.log(ctx.env, errorMessage, handlerElement);
    }

    private static boolean checkIfImplementsRequestHandler(ProcessorContext ctx, TypeElement handlerElement) {
        return handlerElement
                .getInterfaces()
                .stream()
                .anyMatch(t -> checkIfType(ctx, t, RequestHandler.class.getName()));
    }

    private static boolean checkIfImplementsNotificationHandler(ProcessorContext ctx, TypeElement handlerElement) {
        return handlerElement
                .getInterfaces()
                .stream()
                .anyMatch(t -> checkIfType(ctx, t, NotificationHandler.class.getName()));
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
