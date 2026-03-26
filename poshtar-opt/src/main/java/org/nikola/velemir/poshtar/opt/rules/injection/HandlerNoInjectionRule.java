package org.nikola.velemir.poshtar.opt.rules.injection;

import org.nikola.velemir.poshtar.core.mediator.Poshtar;
import org.nikola.velemir.poshtar.core.notification.handler.NotificationHandler;
import org.nikola.velemir.poshtar.core.request.handler.RequestHandler;
import org.nikola.velemir.poshtar.opt.rules.Rule;
import org.nikola.velemir.poshtar.opt.rules.RuleContext;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.*;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic;
import java.util.Set;

public class HandlerNoInjectionRule extends NoInjectionRule {

    private static final String REQ_HANDLER_FQN = RequestHandler.class.getName();
    private static final String NOTIF_HANDLER_FQN = NotificationHandler.class.getName();


    @Override
    public void validateRound(RoundEnvironment roundEnv, RuleContext ctx) {
        Set<String> forbiddenHandlers = ctx.getAllKnownHandlers();
        if (forbiddenHandlers.isEmpty()) return;

        for (Element root : roundEnv.getRootElements()) {
            if (root.getKind() != ElementKind.CLASS) continue;

            TypeElement clazz = (TypeElement) root;

            if (clazz.getQualifiedName().contentEquals(MEDIATOR_FQN)) continue;

            checkClassBody(clazz, forbiddenHandlers, ctx);
        }
    }


    @Override
    protected boolean isForbiddenType(TypeMirror type, Set<String> forbidden, RuleContext ctx) {
        var typeUtils = ctx.env.getTypeUtils();
        var elementUtils = ctx.env.getElementUtils();

        String typeName = typeUtils.erasure(type).toString();
        if (forbidden.contains(typeName)) return true;

        TypeMirror reqHandler = elementUtils.getTypeElement(REQ_HANDLER_FQN).asType();
        TypeMirror notifHandler = elementUtils.getTypeElement(NOTIF_HANDLER_FQN).asType();

        TypeMirror erasedType = typeUtils.erasure(type);
        TypeMirror erasedReq = typeUtils.erasure(reqHandler);
        TypeMirror erasedNotif = typeUtils.erasure(notifHandler);

        return typeUtils.isAssignable(erasedType, erasedReq) ||
                typeUtils.isAssignable(erasedType, erasedNotif);
    }

    @Override
    protected void logViolation(Element target, RuleContext ctx) {
        ctx.env.getMessager().printMessage(
                Diagnostic.Kind.ERROR,
                "PoshtaR VIOLATION: Handlers cannot be injected, set thru methods or constructor, or manually managed. " +
                        "Use 'Poshtar.send(request)' to interact with this logic.",
                target
        );
    }
}