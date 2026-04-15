package org.nikola.velemir.poshtar.opt.internal.rules;

import org.nikola.velemir.poshtar.core.annotations.Handler;
import org.nikola.velemir.poshtar.core.notification.handler.NotificationHandler;
import org.nikola.velemir.poshtar.core.request.handler.RequestHandler;
import org.nikola.velemir.poshtar.opt.internal.context.ProcessorContext;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;

public class HandlerWiringRule extends WiringRule {
    private TypeMirror requestHandlerInterfaceErasure;
    private TypeMirror notificationHandlerInterfaceErasure;

    public HandlerWiringRule() {
        super(Handler.class);
    }

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
