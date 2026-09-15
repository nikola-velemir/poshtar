package io.github.nikola_velemir.poshtar.core.mediator;

import io.github.nikola_velemir.poshtar.core.request.Request;

public interface Sender {
    /**
     * Dispatches a request to its corresponding handler through the pipeline.
     *
     * @param <TRequest>  The type of the request being sent.
     * @param <TResponse> The type of the expected response.
     * @param request     The request object to be processed.
     * @return The response produced by the handler and its associated pipeline.
     * @throws RuntimeException if no handler is registered for the given request type.
     */
    <TRequest extends Request<TResponse>, TResponse> TResponse send(TRequest request);

}
