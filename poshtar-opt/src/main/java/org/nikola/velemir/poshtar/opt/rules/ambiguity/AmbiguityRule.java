package org.nikola.velemir.poshtar.opt.rules.ambiguity;

import org.nikola.velemir.poshtar.core.annotations.Handler;
import org.nikola.velemir.poshtar.core.request.handler.RequestHandler;
import org.nikola.velemir.poshtar.opt.rules.Rule;
import org.nikola.velemir.poshtar.opt.rules.RuleContext;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
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
    // needs shared state across elements — inject a registry
    private final Map<String, String> registry = new HashMap<>();


    private AnnotationMirror getAnnotationMirror(Element element, String annotationName) {
        for (AnnotationMirror mirror : element.getAnnotationMirrors()) {
            if (mirror.getAnnotationType().asElement()
                    .getSimpleName().contentEquals(HANDLER_ANNOTATION_NAME)) {
                return mirror;
            }
        }
        return null;
    }

    private String extractRequestType(TypeElement handlerElement) {
        for (TypeMirror interfaceMirror : handlerElement.getInterfaces()) {
            if (interfaceMirror.getKind() != TypeKind.DECLARED) continue;

            DeclaredType declaredInterface = (DeclaredType) interfaceMirror;
            Element interfaceElement = declaredInterface.asElement();

            if (!interfaceElement.getSimpleName().contentEquals(REQUEST_HANDLER_INTERFACE_NAME)) continue;

            List<? extends TypeMirror> typeArgs = declaredInterface.getTypeArguments();
            if (!typeArgs.isEmpty()) {
                return typeArgs.getFirst().toString();
            }
        }
        return null;
    }

    @Override
    public void validate(TypeElement element, RuleContext ctx) {
        String handler = element.getQualifiedName().toString();
        String request = extractRequestType(element);
        if (request == null) return;

        String existing = ctx.getHandlerFor(request);

        if (existing != null && !existing.equals(handler)) {
            ctx.env.getMessager().printMessage(
                    Diagnostic.Kind.ERROR,
                    "PoshtaR: Ambiguous handlers detected:\n" +
                            "Ambiguity detected for request:'" + request + "'\n— " +
                            existing + "\nvs" +
                            "\n— " + handler,
                    element,
                    getAnnotationMirror(element, HANDLER_ANNOTATION_NAME)
            );
        } else {
            // Update the GLOBAL registry
            ctx.registerHandler(request, handler);
        }
    }

    @Override
    public void validateRound(RoundEnvironment roundEnv, RuleContext ctx) {

    }
}