package org.nikola.velemir.poshtar.opt.utils.registry;

import org.nikola.velemir.poshtar.core.annotations.Behaviour;
import org.nikola.velemir.poshtar.core.annotations.Handler;
import org.nikola.velemir.poshtar.core.request.Request;
import org.nikola.velemir.poshtar.opt.utils.logger.ErrorLogger;
import org.nikola.velemir.poshtar.opt.rules.RuleContext;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;

public class RegistryManager {
    private static final String HANDLER_ANNOTATION_NAME = Handler.class.getName();
    private static final String BEHAVIOUR_ANNOTATION_NAME = Behaviour.class.getName();

    private static final CharSequence REQUEST_INTERFACE_NAME = Request.class.getName();


    public static void preprocessRegistry(RoundEnvironment roundEnv, RuleContext ctx) {

        preprocessHandlers(roundEnv, ctx);

        processBehaviours(roundEnv, ctx);

        processRequests(roundEnv, ctx);
    }

    private static void processRequests(RoundEnvironment roundEnv, RuleContext ctx) {
        TypeElement requestInterface = ctx.getElements()
                .getTypeElement(REQUEST_INTERFACE_NAME);
        if (requestInterface == null) return;

        TypeMirror erasedRequest = ctx.env.getTypeUtils()
                .erasure(requestInterface.asType());

        roundEnv.getRootElements().stream()
                .filter(e -> e.getKind() == ElementKind.CLASS || e.getKind() == ElementKind.RECORD)
                .map(e -> (TypeElement) e)
                .filter(e -> ctx.env.getTypeUtils().isAssignable(
                        ctx.env.getTypeUtils().erasure(e.asType()),
                        erasedRequest
                ))
                .forEach(e -> ctx.registerRequest(e.getQualifiedName().toString()));
    }


    private static void processBehaviours(RoundEnvironment roundEnv, RuleContext ctx) {
        TypeElement behaviourAnnot = ctx.getElements().getTypeElement(BEHAVIOUR_ANNOTATION_NAME);
        if (behaviourAnnot == null) return;

        roundEnv.getElementsAnnotatedWith(behaviourAnnot).stream()
                .filter(e -> e.getKind() == ElementKind.CLASS)
                .map(e -> (TypeElement) e)
                .forEach(b -> {
                    AnnotationMirror mirror = RegistryTypeHelper.getAnnotationMirror(b, BEHAVIOUR_ANNOTATION_NAME);
                    ctx.registerBehaviour(b.getQualifiedName().toString(), b, mirror);
                });
    }

    private static void preprocessHandlers(RoundEnvironment roundEnv, RuleContext ctx) {
        TypeElement handlerAnnot = ctx.getElements().getTypeElement(HANDLER_ANNOTATION_NAME);
        if (handlerAnnot == null) return;
        roundEnv.getElementsAnnotatedWith(handlerAnnot).stream()
                .filter(e -> e.getKind() == ElementKind.CLASS)
                .map(e -> (TypeElement) e)
                .forEach(h -> {
                    try {
                        registerHandler(ctx, h);
                    } catch (ResolutionException ex) {
                        logError(h, ctx, ex);
                    }
                });
    }

    private static void registerHandler(RuleContext ctx, TypeElement h) {
        String requestType = RegistryTypeHelper.extractRequestType(h, ctx);
        if (requestType != null) {
            var mirror = RegistryTypeHelper.getAnnotationMirror(h, HANDLER_ANNOTATION_NAME);
            ctx.registerHandler(h.getQualifiedName().toString(), requestType, h, mirror);
        }
    }


    private static void logError(TypeElement handler, RuleContext ctx, ResolutionException ex) {
        ErrorLogger.log(ctx.env, ex.getMessage(), handler);
    }

}
