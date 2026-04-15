package org.nikola.velemir.poshtar.opt.internal.registry.scanner;

import org.nikola.velemir.poshtar.core.annotations.Behaviour;
import org.nikola.velemir.poshtar.core.annotations.Handler;
import org.nikola.velemir.poshtar.core.request.Request;
import org.nikola.velemir.poshtar.opt.internal.logger.Logger;
import org.nikola.velemir.poshtar.opt.internal.logger.LoggerProvider;
import org.nikola.velemir.poshtar.opt.internal.registry.exception.ResolutionException;
import org.nikola.velemir.poshtar.opt.internal.context.ProcessorContext;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;

class RegistryScannerImpl implements RegistryScanner {
    private static final String HANDLER_ANNOTATION_NAME = Handler.class.getName();
    private static final String BEHAVIOUR_ANNOTATION_NAME = Behaviour.class.getName();

    private static final CharSequence REQUEST_INTERFACE_NAME = Request.class.getName();
    private final Logger logger = LoggerProvider.provideErrorLogger();

    public void scanRegistry(RoundEnvironment roundEnv, ProcessorContext ctx) {

        scanForHandlers(roundEnv, ctx);

        scanForBehaviours(roundEnv, ctx);

        scanForRequests(roundEnv, ctx);
    }

    private void scanForRequests(RoundEnvironment roundEnv, ProcessorContext ctx) {
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


    private void scanForBehaviours(RoundEnvironment roundEnv, ProcessorContext ctx) {
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

    private void scanForHandlers(RoundEnvironment roundEnv, ProcessorContext ctx) {
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

    private void registerHandler(ProcessorContext ctx, TypeElement h) {
        String requestType = RegistryTypeHelper.extractRequestType(h, ctx);
        if (requestType != null) {
            var mirror = RegistryTypeHelper.getAnnotationMirror(h, HANDLER_ANNOTATION_NAME);
            ctx.registerHandler(h.getQualifiedName().toString(), requestType, h, mirror);
        }
    }


    private void logError(TypeElement handler, ProcessorContext ctx, ResolutionException ex) {
        logger.log(ctx.env, ex.getMessage(), handler);
    }

}
