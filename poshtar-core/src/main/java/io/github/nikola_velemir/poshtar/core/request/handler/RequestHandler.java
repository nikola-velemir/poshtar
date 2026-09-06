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
