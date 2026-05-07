package io.github.nikola_velemir.poshtar.opt.internal.rules.deadPipeline;


import io.github.nikola_velemir.poshtar.opt.api.annotations.pipeline.SuppressDead;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;

/**
 * Utility responsible for detecting opt-out metadata for the Dead Pipeline analysis.
 * <p>
 * This checker inspects elements for the presence of the {@link SuppressDead} annotation.
 * </p>
 *
 * @author Nikola Velemir
 * @version ${project.version}
 * @see io.github.nikola_velemir.poshtar.opt.api.annotations.pipeline.SuppressDead
 * @since 1.0.0
 */
public class SuppressionChecker {
    private static final String SUPPRESS_ANNOTATION_NAME = SuppressDead.class.getName();

    /**
     * Determines if the dead pipeline analysis should be bypassed for a given method.
     * <p>
     * The check follows method level and class level supression strategy.
     * </p>
     *
     * @param method The executable element (method) currently under analysis.
     * @return {@code true} if suppression is detected at either level; {@code false} otherwise.
     */
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
