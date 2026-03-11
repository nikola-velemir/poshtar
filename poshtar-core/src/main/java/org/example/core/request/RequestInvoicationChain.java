package org.example.core.request;

@FunctionalInterface
public interface RequestInvoicationChain<TRequest, TResponse> {
    TResponse execute(TRequest request);
}