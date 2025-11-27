package org.example.core.request.handler;

import org.example.core.request.IRequest;

public interface IRequestHandler<TRequest extends IRequest<TResponse>,TResponse> {
    TResponse handle(TRequest request);
}
