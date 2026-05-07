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

package io.github.nikola_velemir.poshtar.core.pipeline.behaviour;

import io.github.nikola_velemir.poshtar.core.pipeline.delegate.RequestDelegate;
import io.github.nikola_velemir.poshtar.core.request.Request;

/**
 * Defines a cross-cutting concern that can be executed before or after a request is handled.
 * <p>
 * Implementations of this interface is a middleware in the request processing pipeline.
 * </p>
 *
 * <p>Each behavior is responsible for calling the {@code next} delegate to continue
 * the execution chain. If a behavior does not call {@code next.handle(request)},
 * the pipeline is "short-circuited," and the subsequent behaviors and the final
 * handler will not be executed. To prevent this, you may use a validation processor in your build.</p>
 *
 * @param <TRequest>  The type of the request being intercepted.
 * @param <TResponse> The type of the response returned by the pipeline.
 * @author Nikola Velemir
 * @version ${project.version}
 * @see io.github.nikola_velemir.poshtar.core.pipeline.delegate.RequestDelegate
 * @since 1.0.0
 */
public interface PipelineBehaviour<TRequest extends Request<TResponse>, TResponse> {
    /**
     * Handles the given request, passes it down the pipeline and produces a response.
     *
     * <p>
     * Behaviors should invoke {@code next.handle(request)}, to pass the request down the pipeline.
     * If method does not call {@code handle} on the delegate, pipeline is effectively stopped.
     * </p>
     *
     * @param request The request object containing the input data.
     * @param next    Next node in pipeline execution.
     * @return The result of the request processing.
     */
    TResponse handle(TRequest request, RequestDelegate<TRequest, TResponse> next);
}
