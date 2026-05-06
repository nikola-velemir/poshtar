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
