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

package io.github.nikola_velemir.poshtar.core.request;


/**
 * Defines a chain of responsibility, through which request will be passed during handling.
 * <p>
 * Class effectively wraps the {@link io.github.nikola_velemir.poshtar.core.pipeline.behaviour.PipelineBehaviour}
 * and {@link io.github.nikola_velemir.poshtar.core.request.handler.RequestHandler} classes,
 * forming an onion structure.
 * </p>
 *
 * <p>
 * Upon calling {@link io.github.nikola_velemir.poshtar.core.mediator.Poshtar}'s {@code send} method,
 * request will be passed down the chain of behavior classes that support {@link TRequest} generic constraint,
 * reaching handler in the end.
 * </p>
 *
 * @param <TRequest>  Type of request chain handles.
 * @param <TResponse> Expected response type after processing the request.
 * @author Nikola Velemir
 * @version ${project.version}
 * @since 1.0.0
 */
@FunctionalInterface
public interface RequestInvocationChain<TRequest extends Request<TResponse>, TResponse> {
    /**
     * Executes the full request processing chain. Calls behaviors in specified order, ending with a handler.
     * @param request Request to be processed.
     * @return Response object, which represents the result of request processing.
     */
    TResponse execute(TRequest request);
}