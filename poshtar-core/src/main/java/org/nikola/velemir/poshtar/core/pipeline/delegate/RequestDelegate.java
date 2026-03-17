package org.nikola.velemir.poshtar.core.pipeline.delegate;

import org.nikola.velemir.poshtar.core.request.Request;

public interface RequestDelegate<TRequest extends Request<TResponse>,TResponse> {
    TResponse handle(TRequest request);
}
