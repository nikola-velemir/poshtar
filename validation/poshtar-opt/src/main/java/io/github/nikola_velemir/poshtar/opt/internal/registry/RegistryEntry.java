package io.github.nikola_velemir.poshtar.opt.internal.registry;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;

public record RegistryEntry(
        String requestFQN,
        String handlerFQN,
        Element handlerElement,
        AnnotationMirror annotationMirror

) {
    public boolean isBehaviour() {
        return "BEHAVIOUR".equals(requestFQN);
    }
}
