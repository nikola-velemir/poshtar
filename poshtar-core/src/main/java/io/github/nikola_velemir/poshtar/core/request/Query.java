package io.github.nikola_velemir.poshtar.core.request;
/**
 * Defines a query that can be dispatched to the mediator.
 * <p>
 * Class implementing this interface represents a query, to be handled by its specific query handler.
 * {@link io.github.nikola_velemir.poshtar.core.mediator.Poshtar}'s {@code send} method processes this query, and returns an object of {@code TResponse} type.
 * </p>
 *
 * <p>For queries that do not return a specific value (even though they should), use
 * {@link io.github.nikola_velemir.poshtar.core.types.Unit} as the response type.
 * </p>
 *
 * @param <TResponse> The type of the response expected after processing this query.
 * @author Nikola Velemir
 * @version ${revision}
 * @since 1.2.0
 */
public  interface Query<TResponse> extends Request<TResponse>{
}

