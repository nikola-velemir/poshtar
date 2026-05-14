/*
 * Copyright 2026 Nikola Velemir
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.nikola_velemir.poshtar.validator.internal.rules.wiring;


import io.github.nikola_velemir.poshtar.validator.internal.logger.Logger;
import io.github.nikola_velemir.poshtar.validator.internal.logger.LoggerProvider;
import io.github.nikola_velemir.poshtar.validator.internal.context.ProcessorContext;
import io.github.nikola_velemir.poshtar.validator.internal.rules.Rule;

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
