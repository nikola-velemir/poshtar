package org.example.core.pipeline.delegate;

import org.example.core.request.IRequest;

public interface RequestDelegate<TRequest extends IRequest<TResponse>,TResponse> {
    TResponse handle(TRequest request);
}
