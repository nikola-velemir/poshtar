/*
 * Copyright 2026 Nikola Velemir
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
