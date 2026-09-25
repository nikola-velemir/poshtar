package io.github.nikola_velemir.poshtar.core.request.handler;

import io.github.nikola_velemir.poshtar.core.request.Query;

public interface QueryHandler<TQuery extends Query<TResponse>, TResponse>
        extends RequestHandler<TQuery, TResponse> {
}
