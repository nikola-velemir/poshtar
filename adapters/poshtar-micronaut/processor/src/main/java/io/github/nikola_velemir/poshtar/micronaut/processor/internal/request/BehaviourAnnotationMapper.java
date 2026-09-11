package io.github.nikola_velemir.poshtar.micronaut.processor.internal.request;

import com.google.auto.service.AutoService;
import io.github.nikola_velemir.poshtar.core.annotations.Behaviour;
import io.micronaut.core.annotation.AnnotationValue;
import io.micronaut.inject.annotation.TypedAnnotationMapper;
import io.micronaut.inject.visitor.VisitorContext;
import jakarta.inject.Singleton;

import java.util.Collections;
import java.util.List;

@AutoService(TypedAnnotationMapper.class)
public class BehaviourAnnotationMapper implements TypedAnnotationMapper<Behaviour> {
    @Override
    public Class<Behaviour> annotationType() {
        return Behaviour.class;
    }

    @Override
    public List<AnnotationValue<?>> map(AnnotationValue<Behaviour> annotation, VisitorContext visitorContext) {
        return Collections.singletonList(AnnotationValue.builder(Singleton.class).build());
    }
}
