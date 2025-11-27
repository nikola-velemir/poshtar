package org.example.core.request.registry;

import org.example.core.request.IRequest;
import org.example.core.request.handler.IRequestHandler;

public interface IHandlerRegistry {
     <TRequest extends IRequest<TResponse>,TResponse> void register(Class<TRequest> requestCLass,IRequestHandler<TRequest,TResponse> requestHandler);
    <TRequest extends IRequest<TResponse>, TResponse>IRequestHandler<TRequest,TResponse> resolve(Class<TRequest> requestType);
}
