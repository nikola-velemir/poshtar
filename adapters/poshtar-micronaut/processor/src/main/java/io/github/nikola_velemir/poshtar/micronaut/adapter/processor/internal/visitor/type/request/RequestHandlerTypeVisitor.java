package io.github.nikola_velemir.poshtar.micronaut.adapter.processor.internal.visitor.type.request;

import com.google.auto.service.AutoService;
import io.github.nikola_velemir.poshtar.core.annotations.Handler;
import io.github.nikola_velemir.poshtar.core.request.handler.RequestHandler;
import io.github.nikola_velemir.poshtar.micronaut.adapter.runtime.internal.discovery.RequestType;
import io.micronaut.core.annotation.AnnotationClassValue;
import io.micronaut.inject.ast.ClassElement;
import io.micronaut.inject.visitor.TypeElementVisitor;
import io.micronaut.inject.visitor.VisitorContext;

import java.util.Map;
import java.util.Optional;

@AutoService(TypeElementVisitor.class)
public class RequestHandlerTypeVisitor implements TypeElementVisitor<Handler, Object> {
    @Override
    public VisitorKind getVisitorKind() {
        // Each handler class is processed independently - no cross-module state needed.
        return VisitorKind.ISOLATING;
    }

    @Override
    public void visitClass(ClassElement element, VisitorContext context) {
        // TypeElementVisitor's <C, E> generics are annotation filters, not type filters -
        // RequestHandler is an interface, so it can't be used there. We filter manually here
        // instead, against every visited class.
        if (element.isAbstract() || element.isInterface() || !element.isAssignable(RequestHandler.class)) {
            return;
        }

        resolveRequestType(element)
                .ifPresentOrElse(
                        requestType -> element.annotate(RequestType.class, builder ->
                                builder.member("value", new AnnotationClassValue<>(requestType.getName()))),
                        () -> context.fail(
                                "Could not resolve the Request generic type argument for this "
                                        + RequestHandler.class.getSimpleName()
                                        + " implementation. It must implement RequestHandler<TRequest, TResponse> "
                                        + "with a concrete TRequest, either directly or through a non-generic "
                                        + "superclass hierarchy.",
                                element));
    }

    /**
     * Walks up from {@code element} looking for the resolved type arguments
     * of {@link RequestHandler}, checking the class itself first and then
     * each superclass in turn (handlers may extend an abstract base handler).
     */
    private Optional<ClassElement> resolveRequestType(ClassElement element) {
        Map<String, ClassElement> typeArguments = element.getTypeArguments(RequestHandler.class);

        if (!typeArguments.isEmpty()) {
            // RequestHandler<TRequest, TResponse> - the request type is declared first.
            return typeArguments.values().stream().findFirst();
        }

        return element.getSuperType().flatMap(this::resolveRequestType);
    }
}
