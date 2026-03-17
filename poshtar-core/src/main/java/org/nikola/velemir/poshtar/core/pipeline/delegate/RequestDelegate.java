package org.nikola.velemir.poshtar.core.pipeline.delegate;

import org.nikola.velemir.poshtar.core.request.IRequest;

public interface RequestDelegate<TRequest extends IRequest<TResponse>,TResponse> {
    TResponse handle(TRequest request);
}
