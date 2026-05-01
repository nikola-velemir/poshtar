package io.github.nikola_velemir.poshtar.core.pipeline.delegate;

import io.github.nikola_velemir.poshtar.core.request.Request;

public interface RequestDelegate<TRequest extends Request<TResponse>,TResponse> {
    TResponse handle(TRequest request);
}
