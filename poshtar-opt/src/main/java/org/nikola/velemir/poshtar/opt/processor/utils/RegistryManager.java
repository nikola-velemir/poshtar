package org.nikola.velemir.poshtar.opt.processor.utils;

import org.nikola.velemir.poshtar.core.annotations.Behaviour;
import org.nikola.velemir.poshtar.core.annotations.Handler;
import org.nikola.velemir.poshtar.core.request.handler.RequestHandler;
import org.nikola.velemir.poshtar.opt.rules.RuleContext;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Properties;

public class RegistryManager {
    private static final String HANDLER_ANNOTATION_NAME = Handler.class.getName();
    private static final String BEHAVIOUR_ANNOTATION_NAME = Behaviour.class.getName();
    private static final String REQUEST_HANDLER_INTERFACE_NAME = RequestHandler.class.getName();

    private static final String REGISTRY_RESOURCE = "META-INF/poshtar-handlers.properties";


    public static void preprocessRegistry(ProcessingEnvironment processingEnv, RoundEnvironment roundEnv, RuleContext ctx) {
        var elements = processingEnv.getElementUtils();

        TypeElement handlerAnnot = elements.getTypeElement(HANDLER_ANNOTATION_NAME);
        if (handlerAnnot != null) {
            preprocessHandlers(roundEnv, ctx, handlerAnnot);
        }

        TypeElement behaviourAnnot = elements.getTypeElement(BEHAVIOUR_ANNOTATION_NAME);
        if (behaviourAnnot != null) {
            processBehaviours(roundEnv, ctx, behaviourAnnot);
        }
    }

    public static Properties loadExistingRegistry(ProcessingEnvironment processingEnv) {
        Properties props = new Properties();
        try {
            FileObject resource = processingEnv.getFiler().getResource(
                    StandardLocation.CLASS_OUTPUT,
                    "",
                    REGISTRY_RESOURCE
            );
            try (InputStream in = resource.openInputStream()) {
                props.load(in);
            }
        } catch (IOException e) {
            // File doesn't exist yet — first pass, this is expected
        }
        return props;
    }

    public static void writeRegistry(ProcessingEnvironment processingEnv, Properties registry) {
        try {
            FileObject resource = processingEnv.getFiler().createResource(
                    StandardLocation.CLASS_OUTPUT,
                    "",
                    REGISTRY_RESOURCE
            );
            try (OutputStream out = resource.openOutputStream()) {
                registry.store(out, "PoshtaR handler registry — do not edit manually");
            }
        } catch (IOException e) {
            processingEnv.getMessager().printMessage(
                    Diagnostic.Kind.WARNING,
                    "PoshtaR: could not write handler registry: " + e.getMessage()
            );
        }
    }

    private static void processBehaviours(RoundEnvironment roundEnv, RuleContext ctx, TypeElement behaviourAnnot) {
        roundEnv.getElementsAnnotatedWith(behaviourAnnot).stream()
                .filter(e -> e.getKind() == ElementKind.CLASS)
                .map(e -> (TypeElement) e)
                .forEach(b -> ctx.registerHandler(b.getQualifiedName().toString(), "BEHAVIOUR"));
    }

    private static void preprocessHandlers(RoundEnvironment roundEnv, RuleContext ctx, TypeElement handlerAnnot) {
        roundEnv.getElementsAnnotatedWith(handlerAnnot).stream()
                .filter(e -> e.getKind() == ElementKind.CLASS)
                .map(e -> (TypeElement) e)
                .forEach(h -> {
                    String requestType = extractRequestType(h, ctx);
                    if (requestType != null) {
                        ctx.registerHandler(h.getQualifiedName().toString(), requestType);
                    }
                });
    }

    private static String extractRequestType(TypeElement handler, RuleContext ctx) {
        var typeUtils = ctx.env.getTypeUtils();
        var elementUtils = ctx.env.getElementUtils();

        TypeElement reqHandlerInterface = elementUtils.getTypeElement(REQUEST_HANDLER_INTERFACE_NAME);
        if (reqHandlerInterface == null) return null;

        TypeMirror erasedReqHandler = typeUtils.erasure(reqHandlerInterface.asType());

        for (TypeMirror iface : handler.getInterfaces()) {
            if (typeUtils.isAssignable(typeUtils.erasure(iface), erasedReqHandler)) {
                if (iface instanceof DeclaredType declared) {
                    List<? extends TypeMirror> typeArgs = declared.getTypeArguments();
                    if (typeArgs.isEmpty()) continue;

                    TypeMirror requestType = typeArgs.getFirst();

                    if (requestType.getKind() == TypeKind.ERROR) {
                        ctx.env.getMessager().printMessage(
                                Diagnostic.Kind.ERROR,
                                "PoshtaR: Cannot resolve request type for handler " + handler.getSimpleName() +
                                        ". Ensure the Request class is imported and compiles.",
                                handler
                        );
                        return null;
                    }
                    return typeUtils.erasure(requestType).toString();
                }
            }
        }
        return null;
    }

}
