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

package io.github.nikola_velemir.poshtar.core.pipeline.factory;

import io.github.nikola_velemir.poshtar.core.pipeline.behaviour.PipelineBehaviour;
import io.github.nikola_velemir.poshtar.core.pipeline.delegate.RequestDelegate;
import io.github.nikola_velemir.poshtar.core.request.Request;
import io.github.nikola_velemir.poshtar.core.request.RequestInvocationChain;
import io.github.nikola_velemir.poshtar.core.request.handler.RequestHandler;

import java.util.List;

/**
 * Factory class responsible for constructing the execution pipeline for a request.
 * <p>
 * This factory builds {@link RequestInvocationChain}. It wraps the final {@link RequestHandler} with
 * the provided {@link PipelineBehaviour}s in reverse order, ensuring that the
 * first behavior in the list is the first one to be executed.
 * </p>
 *
 * @author Nikola Velemir
 * @version ${project.version}
 * @since 1.0.0
 */
public class PipelineFactory {
    /**
     * Creates an invocation chain by wrapping a handler with a list of behaviors.
     * <p>
     * It builds the pipeline from the inside out (starting from the handler) so that
     * the resulting {@link RequestInvocationChain} starts with the first behavior.
     * </p>
     *
     * @param <TRequest>    The type of the request.
     * @param <TResponse>   The type of the response.
     * @param rawHandler    The base handler that will finally process the request.
     * @param rawBehaviours A list of behaviors to wrap around the handler.
     * @return A function representing the complete execution head of the pipeline.
     */
    @SuppressWarnings("unchecked")
    public static <TRequest extends Request<TResponse>, TResponse> RequestInvocationChain<TRequest, TResponse> create(RequestHandler<?, ?> rawHandler, List<PipelineBehaviour<?, ?>> rawBehaviours) {
        RequestHandler<TRequest, TResponse> handler =
                (RequestHandler<TRequest, TResponse>) rawHandler;

        List<PipelineBehaviour<TRequest, TResponse>> behaviours = rawBehaviours.stream()
                .map(b -> (PipelineBehaviour<TRequest, TResponse>) b)
                .toList();
        // Start with the handler as the innermost node
        RequestDelegate<TRequest, TResponse> nextNode = (request) -> handler.handle(request);
        // Wrap behaviors in reverse order so the first in the list is the outer-most
        for (int i = behaviours.size() - 1; i >= 0; i--) {
            nextNode = createNextNode(nextNode, behaviours.get(i));
        }
        final RequestDelegate<TRequest, TResponse> head = nextNode;
        return (request) -> head.handle(request);
    }

    /**
     * Helper method to wrap a specific behavior around the current execution node.
     *
     * @param <TRequest>  The type of the request.
     * @param <TResponse> The type of the response.
     * @param nextNode    The next step in the pipeline (either another behavior or the final handler).
     * @param behaviour   The behavior to be applied at this step.
     * @return A new {@link RequestDelegate} that executes the behavior.
     */
    public static <TRequest extends Request<TResponse>, TResponse> RequestDelegate<TRequest, TResponse> createNextNode(RequestDelegate<TRequest, TResponse> nextNode, PipelineBehaviour<TRequest, TResponse> behaviour) {
        return (request) -> behaviour.handle(request, nextNode);
    }
}
