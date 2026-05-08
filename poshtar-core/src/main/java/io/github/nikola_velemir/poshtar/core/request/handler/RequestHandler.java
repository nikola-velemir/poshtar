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

package io.github.nikola_velemir.poshtar.core.request.handler;

import io.github.nikola_velemir.poshtar.core.request.Request;

/**
 * Defines a component responsible for processing a specific type of {@link Request}.
 * <p>
 * Each implementation of this interface is bound to a single request type and
 * is responsible for executing the logic it contains.
 * Handlers are typically invoked by the {@link io.github.nikola_velemir.poshtar.core.mediator.Poshtar}
 * mediator after the request has passed through the behavior pipeline.
 * </p>
 *
 * @param <TRequest>  The specific type of request this handler processes.
 * @param <TResponse> The type of response produced by this handler.
 * @author Nikola Velemir
 * @version ${project.version}
 * @since 1.0.0
 */
public interface RequestHandler<TRequest extends Request<TResponse>, TResponse> {
    /**
     * Handles the given request and produces a response.
     *
     * @param request The request object containing the input data.
     * @return The result of the request processing.
     */
    TResponse handle(TRequest request);
}
