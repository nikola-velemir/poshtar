package io.github.nikola_velemir.poshtar.opt.internal.rules;


import io.github.nikola_velemir.poshtar.opt.internal.logger.Logger;
import io.github.nikola_velemir.poshtar.opt.internal.logger.LoggerProvider;
import io.github.nikola_velemir.poshtar.opt.internal.context.ProcessorContext;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;

/**
 * Template rule for validating wiring logic.
 * <p>
 * Rule extending this template should prevent wiring violations, where developer should not be allowed to:
 * </p>
 * <ul>
 *     <li>Annotate a class implements {@link io.github.nikola_velemir.poshtar.core.request.handler.RequestHandler}
 *     with {@link io.github.nikola_velemir.poshtar.core.annotations.Behaviour}.</li>
 *     <li>Annotate a class implements {@link io.github.nikola_velemir.poshtar.core.pipeline.behaviour.PipelineBehaviour}
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
    protected final Class annotation;
    protected final Logger logger = LoggerProvider.provideErrorLogger();

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

    protected boolean hasAnnotation(TypeElement typeElement) {
        return typeElement.getAnnotation(annotation) != null;
    }
}
