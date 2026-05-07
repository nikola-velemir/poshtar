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
/**
 * Exception thrown when the mediator attempts to send a request that has no
 * associated handler registered in the {@link io.github.nikola_velemir.poshtar.core.request.registry.RequestRegistry}.
 * <p>
 * Occurs if a handler class is missing the proper annotations,
 * or if the package containing the handler was not included.
 * </p>
 *
 * @author Nikola Velemir
 * @version ${project.version}
 * @since 1.0.0
 */
public class HandlerNotFoundException extends PoshtarException {
    /**
     * Instantiates a new instance of the exception, with the provided class of the orphaned request.
     * @param requestClass Class literal of the orphaned request.
     */
    public HandlerNotFoundException(Class<?> requestClass) {
        super(
                String.format(
                        "No handler found for type: [%s].",
                        requestClass.getSimpleName()
                )
        );
    }
}
