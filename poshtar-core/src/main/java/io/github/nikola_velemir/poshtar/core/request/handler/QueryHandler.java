package io.github.nikola_velemir.poshtar.core.request.handler;

import io.github.nikola_velemir.poshtar.core.request.Query;

/**
 * Defines a component responsible for processing a specific type of {@link Query}.
 * <p>
 * Each implementation of this interface is bound to a single query type and
 * is responsible for executing the logic it contains.
 * Handlers are typically invoked by the {@link io.github.nikola_velemir.poshtar.core.mediator.Poshtar}
 * mediator after the query has passed through the behavior pipeline.
 * </p>
 *
 * @param <TQuery>  The specific type of query this handler processes.
 * @param <TResponse> The type of response produced by this handler.
 * @author Nikola Velemir
 * @version ${revision}
 * @since 1.2.0
 */
public interface QueryHandler<TQuery extends Query<TResponse>, TResponse>
        extends RequestHandler<TQuery, TResponse> {
}
