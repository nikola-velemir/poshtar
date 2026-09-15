package io.github.nikola_velemir.poshtar.micronaut.processor.internal.request;

import io.github.nikola_velemir.poshtar.core.annotations.Handler;
import io.github.nikola_velemir.poshtar.core.request.handler.RequestHandler;
import io.micronaut.core.annotation.AnnotationClassValue;
import io.micronaut.inject.ast.ClassElement;
import io.micronaut.inject.visitor.TypeElementVisitor;
import io.micronaut.inject.visitor.VisitorContext;

import java.util.Map;

public class RequestHandlerVisitor implements TypeElementVisitor<Handler, Object> {
    @Override
    public void visitClass(ClassElement element, VisitorContext context) {
        Map<String, ClassElement> typeArgs = element.getTypeArguments(RequestHandler.class);
        ClassElement reqType = typeArgs.get("TReq");

        if (reqType == null) {
            context.fail("@Handler class must implement RequestHandler<TReq, TRes>", element);
            return;
        }

        // No generated file. No JavaPoet. No manual service descriptor.
        // Just attach metadata to the bean that already exists.
        element.annotate(HandlesRequest.class, builder ->
                builder.member("value", new AnnotationClassValue<>(reqType.getName()))
        );
    }

}
