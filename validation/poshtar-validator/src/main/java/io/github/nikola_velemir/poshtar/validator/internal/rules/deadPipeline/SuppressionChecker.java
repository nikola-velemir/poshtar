/*
 * Copyright (C) 2026 Nikola (nvelem.nikola@gmail.com)
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */

package io.github.nikola_velemir.poshtar.validator.internal.rules.deadPipeline;


import io.github.nikola_velemir.poshtar.validator.api.annotations.pipeline.SuppressDead;

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
 * @see io.github.nikola_velemir.poshtar.validator.api.annotations.pipeline.SuppressDead
 * @since 1.0.0
 */
class SuppressionChecker {
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
