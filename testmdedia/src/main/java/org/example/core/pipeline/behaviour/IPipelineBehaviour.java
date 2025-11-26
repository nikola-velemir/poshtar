package org.example.core.pipeline.behaviour;

import org.example.core.pipeline.delegate.RequestDelegate;

public interface IPipelineBehaviour <TRequest ,TResponse >{
    boolean supports(Class<?> requestType);

    TResponse handle(TRequest request, RequestDelegate<TResponse> next);
}
