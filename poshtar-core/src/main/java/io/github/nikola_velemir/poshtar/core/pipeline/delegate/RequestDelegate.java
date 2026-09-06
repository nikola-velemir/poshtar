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
