/*
 * Copyright (C) 2026 Nikola (nvelem.nikola@gmail.com)
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */

package io.github.nikola_velemir.poshtar.validator.internal.registry;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
/**
 * Metadata container representing a discovered component.
 * <p>
 * This record stores the relationship between a component (Handler or Behavior)
 * and its associated request type. It carries both the string-based Fully Qualified Names (FQN)
 * for logical checks and the {@link Element} is required for the compiler to
 * provide source code feedback.
 * </p>
 *
 * @param requestFQN     The fully qualified name of the request being handled.
 *                       Uses the special constant {@code "BEHAVIOUR"} for pipeline behaviors.
 * @param handlerFQN     The fully qualified name of the handler or behavior class.
 * @param handlerElement The compile-time element representing the class, used for error reporting.
 * @param annotationMirror The specific instance of the annotation (@Handler or @Behaviour)
 *                         found on the element.
 *
 * @author Nikola Velemir
 * @version ${project.version}
 * @since 1.0.0
 */
public record RegistryEntry(
        String requestFQN,
        String handlerFQN,
        Element handlerElement,
        AnnotationMirror annotationMirror

) {
    /**
     * Determines if this entry represents a pipeline behavior rather than a standard request handler.
     * <p>
     * Behaviors are treated differently by the validation logic as they allow for
     * multiple implementations, whereas standard handlers
     * usually follow a one-to-one mapping rule.
     * </p>
     *
     * @return {@code true} if the entry is a behavior; {@code false} if it is a request handler.
     */
    public boolean isBehaviour() {
        return "BEHAVIOUR".equals(requestFQN);
    }
}
