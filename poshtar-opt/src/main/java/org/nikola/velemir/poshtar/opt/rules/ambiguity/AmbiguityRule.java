package org.nikola.velemir.poshtar.opt.rules.ambiguity;

import org.nikola.velemir.poshtar.core.annotations.Handler;
import org.nikola.velemir.poshtar.core.request.handler.RequestHandler;
import org.nikola.velemir.poshtar.opt.rules.Rule;
import org.nikola.velemir.poshtar.opt.rules.RuleContext;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AmbiguityRule implements Rule {
    public static final String HANDLER_ANNOTATION_NAME = Handler.class.getName();
    public static final String REQUEST_HANDLER_INTERFACE_NAME = RequestHandler.class.getSimpleName();

    @Override
    public void validate(RoundEnvironment roundEnv, RuleContext ctx) {
        TypeElement handlerAnnot = ctx.env.getElementUtils().getTypeElement(HANDLER_ANNOTATION_NAME);
        if (handlerAnnot == null) return;

        // Pull all elements discovered in this round
        for (Element e : roundEnv.getElementsAnnotatedWith(handlerAnnot)) {
            if (e.getKind() != ElementKind.CLASS) continue;

            TypeElement handlerElement = (TypeElement) e;
            validateMapping(handlerElement, ctx);
        }
    }

    private void validateMapping(TypeElement handler, RuleContext ctx) {
        String handlerFqn = handler.getQualifiedName().toString();
        String requestFqn = extractRequestType(handler, ctx);

        if (requestFqn == null) return;

        String existing = ctx.getHandlerFor(requestFqn);

        if (existing != null && !existing.equals(handlerFqn)) {
            ctx.env.getMessager().printMessage(
                    Diagnostic.Kind.ERROR,
                    String.format("PoshtaR: Ambiguity detected! Request '%s' is handled by both:%n - %s%n - %s",
                            requestFqn, existing, handlerFqn),
                    handler,
                    getAnnotationMirror(handler)
            );
        } else {
            // Mapping is safe: Request -> Handler
            ctx.registerHandler(requestFqn, handlerFqn);
        }
    }

    private String extractRequestType(TypeElement handler, RuleContext ctx) {
        var typeUtils = ctx.env.getTypeUtils();
        var elementUtils = ctx.env.getElementUtils();

        // 1. Get the FQN of your interface safely
        TypeElement reqHandlerInterface = elementUtils.getTypeElement("org.nikola.velemir.poshtar.core.request.handler.RequestHandler");
        if (reqHandlerInterface == null) return null;

        TypeMirror erasedReqHandler = typeUtils.erasure(reqHandlerInterface.asType());

        // 2. Iterate through interfaces (including those on parent classes if needed)
        for (TypeMirror iface : handler.getInterfaces()) {
            // Use isAssignable to handle the hierarchy correctly
            if (typeUtils.isAssignable(typeUtils.erasure(iface), erasedReqHandler)) {
                if (iface instanceof DeclaredType declared) {
                    List<? extends TypeMirror> typeArgs = declared.getTypeArguments();
                    if (typeArgs.isEmpty()) continue;

                    TypeMirror requestType = typeArgs.getFirst();

                    // 3. CRITICAL: Handle unresolved symbols
                    // If the user forgot an import or made a typo, the Kind will be ERROR.
                    if (requestType.getKind() == TypeKind.ERROR) {
                        ctx.env.getMessager().printMessage(
                                Diagnostic.Kind.ERROR,
                                "PoshtaR: Cannot resolve request type for handler " + handler.getSimpleName() +
                                        ". Ensure the Request class is imported and compiles.",
                                handler
                        );
                        return null;
                    }

                    // 4. Use erasure to get the absolute FQN (e.g., com.app.MyRequest)
                    return typeUtils.erasure(requestType).toString();
                }
            }
        }
        return null;
    }

    private AnnotationMirror getAnnotationMirror(Element element) {
        for (AnnotationMirror mirror : element.getAnnotationMirrors()) {
            // Compare FQN to FQN
            String mirrorFqn = ((TypeElement) mirror.getAnnotationType().asElement()).getQualifiedName().toString();
            if (mirrorFqn.equals(HANDLER_ANNOTATION_NAME)) {
                return mirror;
            }
        }
        return null;
    }
}