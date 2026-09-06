/*
 * Copyright (C) 2026 Nikola (nvelem.nikola@gmail.com)
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */

package io.github.nikola_velemir.poshtar.validator.internal.rules.semantical.wiring;

import io.github.nikola_velemir.poshtar.core.annotations.Handler;
import io.github.nikola_velemir.poshtar.core.notification.handler.NotificationHandler;
import io.github.nikola_velemir.poshtar.core.request.handler.RequestHandler;
import io.github.nikola_velemir.poshtar.validator.internal.context.ProcessorContext;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
/**
 * Template rule for validating wiring logic.
 * <p>
 * Rule extending this template should prevent wiring violations, where developer should not be allowed to:
 * </p>
 * <ul>
 *     <li>Annotate a class implements {@link io.github.nikola_velemir.poshtar.core.request.handler.RequestHandler}
 *     with {@link io.github.nikola_velemir.poshtar.core.annotations.Behaviour}.</li>
 *     <li>Annotate a class implements {@link io.github.nikola_velemir.poshtar.core.pipeline.behaviour.PipelineBehaviour}
 *     with {@link io.github.nikola_velemir.poshtar.core.annotations.Handler}.</li>
 *
 * </ul>
 * <p>
 * Rule prevents from creating ambiguities between behavior and handler classes.
 * </p>
 *
 * @author Nikola Velemir
 * @version ${project.version}
 * @see io.github.nikola_velemir.poshtar.core.exceptions.AmbiguousHandlerException
 * @since 1.0.0
 */
class HandlerWiringRule extends WiringRule {
    private TypeMirror requestHandlerInterfaceErasure;
    private TypeMirror notificationHandlerInterfaceErasure;

    public HandlerWiringRule() {
        super(Handler.class);
    }

    /**
     * Initializes the request handler interface type erasures, used to validate if wiring is correct.
     * @param ctx Instance of context containing all related request, handler and behavior FQNs.
     */
    @Override
    protected void initErasures(ProcessorContext ctx) {
        var types = ctx.getTypes();
        TypeElement requestHandlerIface = ctx.getElements().getTypeElement(RequestHandler.class.getCanonicalName());
        TypeElement notificationHandlerIface = ctx.getElements().getTypeElement(NotificationHandler.class.getCanonicalName());

        if (requestHandlerIface == null || notificationHandlerIface == null) return;

        requestHandlerInterfaceErasure = types.erasure(requestHandlerIface.asType());
        notificationHandlerInterfaceErasure = types.erasure(notificationHandlerIface.asType());
    }

    @Override
    protected void validateAnnotationAndImplementation(ProcessorContext ctx, TypeElement typeElement) {
        var types = ctx.getTypes();
        boolean implementsRequestHandler = types.isAssignable(
                types.erasure(typeElement.asType()),
                requestHandlerInterfaceErasure
        );
        boolean implementsNotificationHandler = types.isAssignable(
                types.erasure(typeElement.asType()),
                notificationHandlerInterfaceErasure
        );

        boolean hasAnnotation = this.hasAnnotation(typeElement);

        if ((implementsNotificationHandler || implementsRequestHandler) && !hasAnnotation) {
            logger.log(ctx.env, "Missing @Handler annotation on Handler implementation.", typeElement);
        }

        if (hasAnnotation && (!implementsRequestHandler && !implementsNotificationHandler)) {
            logger.log(ctx.env, "Class annotated with @Handler must implement RequestHandler or NotificationHandler.", typeElement);
        }
    }
}
