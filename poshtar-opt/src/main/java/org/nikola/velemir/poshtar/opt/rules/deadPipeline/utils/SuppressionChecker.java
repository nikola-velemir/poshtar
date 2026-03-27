package org.nikola.velemir.poshtar.opt.rules.deadPipeline.utils;

import org.nikola.velemir.poshtar.opt.annotations.pipeline.SuppressDead;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;

public class SuppressionChecker {
    private static final String SUPPRESS_ANNOTATION_NAME = SuppressDead.class.getName();

    public static boolean hasSuppression(ExecutableElement method) {
        boolean methodSuppressed = method.getAnnotationMirrors().stream()
                .anyMatch(mirror -> mirror.getAnnotationType().asElement()
                        .toString().equals(SUPPRESS_ANNOTATION_NAME));
        if (methodSuppressed) return true;

        Element enclosing = method.getEnclosingElement();
        return enclosing.getAnnotationMirrors().stream()
                .anyMatch(mirror -> mirror.getAnnotationType().asElement()
                        .toString().equals(SUPPRESS_ANNOTATION_NAME));
    }
}
