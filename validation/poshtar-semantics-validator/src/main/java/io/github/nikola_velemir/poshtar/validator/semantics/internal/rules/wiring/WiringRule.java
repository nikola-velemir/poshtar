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

package io.github.nikola_velemir.poshtar.validator.semantics.internal.rules.wiring;

import io.github.nikola_velemir.poshtar.validator.base.internal.context.ProcessorContext;
import io.github.nikola_velemir.poshtar.validator.base.internal.logger.Logger;
import io.github.nikola_velemir.poshtar.validator.base.internal.logger.LoggerProvider;
import io.github.nikola_velemir.poshtar.validator.base.internal.rules.Rule;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;

/**
 * Template rule for validating wiring logic.
 * <p>
 * Rule extending this template should prevent wiring violations, where developer should not be allowed to:
 * </p>
 * <ul>
 *     <li>Annotated a class implements {@link io.github.nikola_velemir.poshtar.core.request.handler.RequestHandler}
 *     with {@link io.github.nikola_velemir.poshtar.core.annotations.Behaviour}.</li>
 *     <li>Annotated a class implements {@link io.github.nikola_velemir.poshtar.core.pipeline.behaviour.PipelineBehaviour}
 *     with {@link io.github.nikola_velemir.poshtar.core.annotations.Handler}.</li>
 *
 * </ul>
 * <p>
 * Rule prevents from creating ambiguities between behavior and handler classes.
 * </p>
 *
 * @author Nikola Velemir
 * @version ${project.version}
 * @see io.github.nikola_velemir.poshtar.core.exceptions.AmbiguousHandlerException
 * @since 1.0.0
 */
@SuppressWarnings({"rawtypes", "unchecked"})
abstract class WiringRule implements Rule {
    /**
     * Annotation that will be checked for wiring violations.
     */
    protected final Class annotation;
    /**
     * Provided logger that will dispatch errors to the compiler, if wiring violations are found.
     */
    protected final Logger logger = LoggerProvider.provideErrorLogger();

    /**
     * Instantiates a wiring rule, with a provided annotation.
     *
     * @param annotation Annotation that will be inspected for wiring violations.
     */
    public WiringRule(Class annotation) {
        this.annotation = annotation;
    }

    /**
     * Initiates Java erasures and checks if correct annotation matches the correct interface.
     *
     * @param roundEnv The environment providing access to elements in the current processing round.
     * @param ctx      Instance of context containing all related request, handler and behavior FQNs.
     */
    @Override
    public void validate(RoundEnvironment roundEnv, ProcessorContext ctx) {
        initErasures(ctx);

        for (var element : roundEnv.getRootElements()) {
            if (element instanceof TypeElement typeElement && element.getKind().equals(ElementKind.CLASS)) {
                validateAnnotationAndImplementation(ctx, typeElement);
            }
        }
    }

    /**
     * Checks if correct annotation matches the correct interface.
     *
     * @param typeElement Type that is being inspected.
     * @param ctx         Instance of context containing all related request, handler and behavior FQNs.
     */
    protected abstract void validateAnnotationAndImplementation(ProcessorContext ctx, TypeElement typeElement);

    /**
     * Used to initiate type mirror in inheriting classes,
     * that will be used for inspection during rule validation.
     *
     * @param ctx Instance of context containing all related request, handler and behavior FQNs.
     */
    protected abstract void initErasures(ProcessorContext ctx);

    /**
     * Check if provided element is annotated with designated annotation.
     *
     * @param typeElement Element which will be checked for annotation's presence.
     * @return {@code true} if element is annotated with the annotation, otherwise {@code false}.
     */
    protected boolean hasAnnotation(TypeElement typeElement) {
        return typeElement.getAnnotation(annotation) != null;
    }
}
