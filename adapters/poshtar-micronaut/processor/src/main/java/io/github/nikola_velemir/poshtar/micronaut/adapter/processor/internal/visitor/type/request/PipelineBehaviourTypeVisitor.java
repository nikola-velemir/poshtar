package io.github.nikola_velemir.poshtar.micronaut.adapter.processor.internal.visitor.type.request;

import com.google.auto.service.AutoService;
import io.github.nikola_velemir.poshtar.core.annotations.Behaviour;
import io.github.nikola_velemir.poshtar.core.pipeline.behaviour.PipelineBehaviour;
import io.github.nikola_velemir.poshtar.core.request.Request;
import io.github.nikola_velemir.poshtar.micronaut.adapter.runtime.internal.discovery.RequestType;
import io.micronaut.core.annotation.AnnotationClassValue;
import io.micronaut.inject.ast.ClassElement;
import io.micronaut.inject.visitor.TypeElementVisitor;
import io.micronaut.inject.visitor.VisitorContext;

import java.util.Map;
import java.util.Optional;

@AutoService(TypeElementVisitor.class)
public class PipelineBehaviourTypeVisitor implements TypeElementVisitor<Behaviour, Object> {
    @Override
    public VisitorKind getVisitorKind() {
        return VisitorKind.ISOLATING;
    }

    @Override
    public void visitClass(ClassElement element, VisitorContext context) {
        if (element.isAbstract() || element.isInterface() || !element.isAssignable(PipelineBehaviour.class)) {
            return;
        }

        resolveRequestType(element).ifPresent(requestType -> {
            // If the type argument is a generic type parameter (like <TRequest>), fall back to Request.class (Global)
            String className = requestType.isTypeVariable() ? Request.class.getName() : requestType.getName();

            element.annotate(RequestType.class, builder ->
                    builder.member("value", new AnnotationClassValue<>(className))
            );
        });
    }

    private Optional<ClassElement> resolveRequestType(ClassElement element) {
        Map<String, ClassElement> typeArguments = element.getTypeArguments(PipelineBehaviour.class);

        if (!typeArguments.isEmpty()) {
            return typeArguments.values().stream().findFirst();
        }

        return element.getSuperType().flatMap(this::resolveRequestType);
    }
}