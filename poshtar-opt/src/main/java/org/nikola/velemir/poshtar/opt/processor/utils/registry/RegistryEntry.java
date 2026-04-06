package org.nikola.velemir.poshtar.opt.processor.utils.registry;

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
