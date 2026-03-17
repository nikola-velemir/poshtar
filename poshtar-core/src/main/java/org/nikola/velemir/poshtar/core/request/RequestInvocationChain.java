package org.nikola.velemir.poshtar.core.request;

@FunctionalInterface
public interface RequestInvocationChain<TRequest extends IRequest<TResponse>, TResponse> {
    TResponse execute(TRequest request);
}