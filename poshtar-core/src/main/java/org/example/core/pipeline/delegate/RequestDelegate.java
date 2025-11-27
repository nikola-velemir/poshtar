package org.example.core.pipeline.delegate;

@FunctionalInterface
public interface RequestDelegate<TResponse> {
    TResponse handle();
}
