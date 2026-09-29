package io.github.nikola_velemir.poshtar.validator.internal.registry;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;

/**
 * Entry for a notification registry, containing all the requuired data for notification handler mappings.
 * NotificationRegistryEntry
 * @param notificationFqn FQN of the notification class.
 * @param handlerFQN FQN of the notification handler class.
 * @param handlerElement Element in code for of the handler class.
 * @param annotationMirror Annotaion mirror.
 */
public record NotificationRegistryEntry(
        String notificationFqn,
        String handlerFQN,
        Element handlerElement,
        AnnotationMirror annotationMirror

) {
}
