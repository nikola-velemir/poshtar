package org.nikola.velemir.poshtar.opt.rules;

import org.nikola.velemir.poshtar.core.notification.handler.NotificationHandler;
import org.nikola.velemir.poshtar.core.request.handler.RequestHandler;
import org.nikola.velemir.poshtar.opt.processor.utils.ErrorLogger;

import javax.lang.model.element.*;
import javax.lang.model.type.TypeMirror;
import java.util.Set;

 class HandlerNoInjectionRule extends NoInjectionRule {

    private static final String REQ_HANDLER_FQN = RequestHandler.class.getName();
    private static final String NOTIF_HANDLER_FQN = NotificationHandler.class.getName();


    @Override
    protected boolean isForbiddenType(TypeMirror type, Set<String> forbidden, RuleContext ctx) {
        var typeUtils = ctx.env.getTypeUtils();
        var elementUtils = ctx.env.getElementUtils();

        TypeMirror reqHandler = elementUtils.getTypeElement(REQ_HANDLER_FQN).asType();
        TypeMirror notifHandler = elementUtils.getTypeElement(NOTIF_HANDLER_FQN).asType();

        TypeMirror erasedType = typeUtils.erasure(type);
        TypeMirror erasedReq = typeUtils.erasure(reqHandler);
        TypeMirror erasedNotif = typeUtils.erasure(notifHandler);

        return typeUtils.isAssignable(erasedType, erasedReq) ||
                typeUtils.isAssignable(erasedType, erasedNotif);
    }

    @Override
    protected void logError(Element target, RuleContext ctx) {
        String errorMessage = "PoshtaR VIOLATION: Handlers cannot be injected, set thru methods or constructor, or manually managed. " +
                "Use 'Poshtar.send(request)' to interact with this logic.";
        ErrorLogger.logError(ctx.env, errorMessage, target);

    }
}