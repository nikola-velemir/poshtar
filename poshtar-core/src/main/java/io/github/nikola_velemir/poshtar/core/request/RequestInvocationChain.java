package io.github.nikola_velemir.poshtar.core.request;

@FunctionalInterface
public interface RequestInvocationChain<TRequest extends Request<TResponse>, TResponse> {
    TResponse execute(TRequest request);
}