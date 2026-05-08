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

package io.github.nikola_velemir.poshtar.core.exceptions;

import java.util.List;
/**
 * Exception thrown when multiple handlers are discovered or registered for a single {@link io.github.nikola_velemir.poshtar.core.request.Request} type.
 * <p>
 * The Poshtar requires a strict one-to-one mapping for requests to ensure
 * deterministic behavior.
 * </p>
 *
 * @author Nikola Velemir
 * @version ${project.version}
 * @since 1.0.0
 */
public class AmbiguousHandlerException extends PoshtarException {
    /**
     * Constructs a new exception detailing the conflicting handlers for a specific request type.
     *
     * @param requestClass The class of the request that has multiple handlers assigned.
     * @param handlers     A list of the handler classes that were found to be in conflict.
     */
    public AmbiguousHandlerException(Class<?> requestClass, List<Class<?>> handlers) {
        super(
                String.format("There are multiple handlers registered for type [%s]: %s",
                        requestClass.getSimpleName(), handlers.toString())
        );
    }
}
