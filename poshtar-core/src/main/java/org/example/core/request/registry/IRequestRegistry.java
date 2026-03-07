package org.example.core.request.registry;

import org.example.core.request.IRequest;
import org.example.core.request.RequestChain;
import org.example.core.request.handler.IRequestHandler;

public interface IRequestRegistry {
    <TRequest extends IRequest<TResponse>, TResponse> RequestChain<TRequest,TResponse> resolve(Class<TRequest> requestType);
}
