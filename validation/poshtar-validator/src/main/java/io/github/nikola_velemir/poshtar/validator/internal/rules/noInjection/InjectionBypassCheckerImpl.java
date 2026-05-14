package io.github.nikola_velemir.poshtar.validator.internal.rules.noInjection;

import io.github.nikola_velemir.poshtar.validator.api.annotations.injection.OverruleNoInjection;
import io.github.nikola_velemir.poshtar.validator.internal.context.ProcessorContext;

import javax.lang.model.element.TypeElement;

class InjectionBypassCheckerImpl implements InjectionBypassChecker {
    private static final String BYPASS_ANNOTATION_FQN = OverruleNoInjection.class.getName();

    public boolean isBypassed(TypeElement clazz, ProcessorContext ctx) {
        return hasAnnotation(clazz, ctx) && isInTestPackage(clazz, ctx);
    }

    private static boolean hasAnnotation(TypeElement clazz, ProcessorContext ctx) {
        return clazz.getAnnotationMirrors()
                .stream()
                .map(mirror -> mirror.getAnnotationType().asElement().toString())
                .anyMatch(BYPASS_ANNOTATION_FQN::equals);
    }

    private static boolean isInTestPackage(TypeElement clazz, ProcessorContext ctx) {
        String fqn = clazz.getQualifiedName().toString();
        return fqn.contains(".test.");
    }
}
