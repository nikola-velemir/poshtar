package org.nikola.velemir.poshtar.core.request.handler;

import org.nikola.velemir.poshtar.core.request.Request;

public interface RequestHandler<TRequest extends Request<TResponse>,TResponse> {
    TResponse handle(TRequest request);
}
