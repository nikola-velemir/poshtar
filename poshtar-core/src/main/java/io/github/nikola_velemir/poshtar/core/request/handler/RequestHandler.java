package io.github.nikola_velemir.poshtar.core.request.handler;

import io.github.nikola_velemir.poshtar.core.request.Request;

public interface RequestHandler<TRequest extends Request<TResponse>,TResponse> {
    TResponse handle(TRequest request);
}
