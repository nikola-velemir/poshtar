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
