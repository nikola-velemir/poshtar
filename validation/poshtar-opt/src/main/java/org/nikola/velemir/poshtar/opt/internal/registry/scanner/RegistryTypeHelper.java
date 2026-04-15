package org.nikola.velemir.poshtar.opt.internal.registry.scanner;

import org.nikola.velemir.poshtar.core.request.handler.RequestHandler;
import org.nikola.velemir.poshtar.opt.internal.registry.exception.ResolutionException;
import org.nikola.velemir.poshtar.opt.processor.ProcessorContext;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import java.util.List;

class RegistryTypeHelper {
    private static final String REQUEST_HANDLER_INTERFACE_NAME = RequestHandler.class.getName();
    public static final String RESOLUTION_ERROR_MESSAGE = "PoshtaR: Cannot resolve request type for handle %s. Ensure the Request class is imported and compiles.";

    public static AnnotationMirror getAnnotationMirror(TypeElement element, String annotation) {
        return element.getAnnotationMirrors().stream()
                .filter(m -> m.getAnnotationType().toString().equals(annotation))
                .findFirst()
                .orElse(null);
    }

    public static String extractRequestType(TypeElement handler, ProcessorContext ctx) throws ResolutionException {
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
                        Name handlerName = handler.getSimpleName();
                        String errorMessage = String.format(RESOLUTION_ERROR_MESSAGE, handlerName);
                        throw new ResolutionException(errorMessage);
                    }
                    return typeUtils.erasure(requestType).toString();
                }
            }
        }
        return null;
    }
}
