package org.example.core.pipeline.behaviour;

import org.example.core.pipeline.delegate.RequestDelegate;
import org.example.core.request.IRequest;

public interface IPipelineBehaviour<TRequest extends IRequest<TResponse>, TResponse> {

    TResponse handle(TRequest request, RequestDelegate<TRequest, TResponse> next);
}
