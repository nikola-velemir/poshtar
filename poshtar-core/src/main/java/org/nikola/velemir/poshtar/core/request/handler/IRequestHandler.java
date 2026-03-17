package org.nikola.velemir.poshtar.core.request.handler;

import org.nikola.velemir.poshtar.core.request.IRequest;

public interface IRequestHandler<TRequest extends IRequest<TResponse>,TResponse> {
    TResponse handle(TRequest request);
}
