package io.github.nikola_velemir.poshtar.micronaut.processor.internal.request;

import com.google.auto.service.AutoService;
import io.github.nikola_velemir.poshtar.core.annotations.Handler;
import io.micronaut.core.annotation.AnnotationValue;
import io.micronaut.inject.annotation.TypedAnnotationMapper;
import io.micronaut.inject.visitor.VisitorContext;
import jakarta.inject.Singleton;

import java.util.Collections;
import java.util.List;
@AutoService(TypedAnnotationMapper.class)

public class HandlerAnnotationMapper implements TypedAnnotationMapper<Handler> {
    @Override
    public Class<Handler> annotationType() {
        return Handler.class;
    }

    @Override
    public List<AnnotationValue<?>> map(AnnotationValue<Handler> annotation, VisitorContext visitorContext) {
        return Collections.singletonList(AnnotationValue.builder(Singleton.class).build());    }
}
