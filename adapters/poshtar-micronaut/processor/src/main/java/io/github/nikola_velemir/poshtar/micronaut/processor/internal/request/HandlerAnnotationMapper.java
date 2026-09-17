package io.github.nikola_velemir.poshtar.micronaut.processor.internal.request;

import com.google.auto.service.AutoService;
import io.github.nikola_velemir.poshtar.core.annotations.Handler;
import io.micronaut.core.annotation.AnnotationValue;
import io.micronaut.inject.annotation.TypedAnnotationMapper;
import io.micronaut.inject.visitor.VisitorContext;
import jakarta.inject.Singleton;

import java.util.Collections;
import java.util.List;

/**
 * Annotation mapper that transforms the {@link Handler}
 * annotation
 * into Micronaut's standard {@link Singleton} annotation at compile time.
 * <p>
 * This allows classes annotated with {@code @Handler} to be automatically
 * registered and managed
 * as singleton beans within the Micronaut Dependency Injection container
 * without requiring explicit
 * standard Jakarta DI annotations on the source classes.
 * </p>
 */
@AutoService(TypedAnnotationMapper.class)
public class HandlerAnnotationMapper implements TypedAnnotationMapper<Handler> {
    /**
     * Specifies the target annotation type handled by this mapper.
     *
     * @return the {@link Handler} class type
     */
    @Override
    public Class<Handler> annotationType() {
        return Handler.class;
    }

    /**
     * Maps the {@link Handler} annotation to a list of additional annotations to be
     * applied
     * to the target element.
     *
     * @return a single-element list containing the generated {@link Singleton}
     *         annotation value
     */
    @Override
    public List<AnnotationValue<?>> map(AnnotationValue<Handler> annotation, VisitorContext visitorContext) {
        return Collections.singletonList(AnnotationValue.builder(Singleton.class).build());
    }
}
