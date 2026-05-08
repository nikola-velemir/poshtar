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

package io.github.nikola_velemir.poshtar.core.pipeline.delegate;

import io.github.nikola_velemir.poshtar.core.request.Request;

/**
 * Represents a delegate that handles a request within the execution pipeline.
 * <p>
 * Acts as a pointer to the next step in the process; Whether that step
 * is another {@link io.github.nikola_velemir.poshtar.core.pipeline.behaviour.PipelineBehaviour}
 * or the final {@link io.github.nikola_velemir.poshtar.core.request.handler.RequestHandler}.
 * </p>
 *
 * @param <TRequest>  The type of the request being handled.
 * @param <TResponse> The type of the response returned by the handler.
 * @author Nikola Velemir
 * @version ${project.version}
 * @since 1.0.0
 */
public interface RequestDelegate<TRequest extends Request<TResponse>, TResponse> {
    /**
     * Dispatches the request to the next stage of the pipeline.
     *
     * @param request The request object to be processed.
     * @return The response produced by the subsequent steps in the pipeline.
     */
    TResponse handle(TRequest request);
}
