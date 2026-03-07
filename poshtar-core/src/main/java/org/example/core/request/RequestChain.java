package org.example.core.request;

@FunctionalInterface
public interface RequestChain<TRequest, TResponse> {
    TResponse execute(TRequest request) throws Exception;
}