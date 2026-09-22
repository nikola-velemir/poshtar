package io.github.nikola_velemir.poshtar.micronaut.processor.internal.request;

import com.google.auto.service.AutoService;
import io.github.nikola_velemir.poshtar.core.annotations.Behaviour;
import io.micronaut.core.annotation.AnnotationValue;
import io.micronaut.inject.annotation.TypedAnnotationMapper;
import io.micronaut.inject.visitor.VisitorContext;
import jakarta.inject.Singleton;

import java.util.Collections;
import java.util.List;

/**
 * Annotation mapper that transforms the {@link Behaviour}
 * annotation
 * into Micronaut's standard {@link Singleton} annotation at compile time.
 * <p>
 * This ensures that components tagged with {@code @Behaviour} are picked up by
 * the Micronaut
 * compilation pipeline and registered as singleton beans in the application
 * context.
 * </p>
 */
@AutoService(TypedAnnotationMapper.class)
public class BehaviourAnnotationMapper implements TypedAnnotationMapper<Behaviour> {
    /**
     * Specifies the target annotation type handled by this mapper.
     *
     * @return the {@link Behaviour} class type
     */
    @Override
    public Class<Behaviour> annotationType() {
        return Behaviour.class;
    }

    /**
     * Maps the {@link Behaviour} annotation to a list of additional annotations to
     * be applied
     * to the target element.
     *
     * @return a single-element list containing the generated {@link Singleton}
     *         annotation value
     */
    @Override
    public List<AnnotationValue<?>> map(AnnotationValue<Behaviour> annotation, VisitorContext visitorContext) {
        System.out.println("It fucking ran didnt it?");

        return Collections.singletonList(AnnotationValue.builder(Singleton.class).build());
    }
}
