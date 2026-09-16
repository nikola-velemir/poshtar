package io.github.nikola_velemir.poshtar.validator.internal.registry;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;

public record NotificationRegistryEntry(
        String requestFQN,
        String handlerFQN,
        Element handlerElement,
        AnnotationMirror annotationMirror

) {
}
