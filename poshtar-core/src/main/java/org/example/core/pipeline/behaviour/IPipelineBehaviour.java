package org.example.core.pipeline.behaviour;

import org.example.core.pipeline.delegate.RequestDelegate;

public interface IPipelineBehaviour<TRequest, TResponse> {

    TResponse handle(TRequest request, RequestDelegate<TRequest, TResponse> next);
}
