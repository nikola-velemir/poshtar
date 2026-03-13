package org.example.core.request;

@FunctionalInterface
public interface RequestInvocationChain<TRequest, TResponse> {
    TResponse execute(TRequest request);
}