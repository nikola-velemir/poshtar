package org.example.core.pipeline.delegate;

public interface RequestDelegate<TRequest,TResponse> {
    TResponse handle(TRequest request);
}
