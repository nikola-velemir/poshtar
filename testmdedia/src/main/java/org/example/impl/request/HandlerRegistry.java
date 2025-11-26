package org.example.impl.request;

import org.example.core.request.handler.IRequestHandler;
import org.example.core.request.registry.IHandlerRegistry;
import org.example.core.request.IRequest;

import java.util.HashMap;
import java.util.Map;

public class HandlerRegistry implements IHandlerRegistry {
    private final Map<Class<?>, IRequestHandler> handlers = new HashMap<>();

    public <TRequest extends IRequest<TResponse>,TResponse> void register(Class<TRequest> requestClass,IRequestHandler<TRequest,TResponse> requestHandler) {
        handlers.put(requestClass, requestHandler);
    }
    @Override
    public <TRequest extends IRequest<TResponse>, TResponse> IRequestHandler<TRequest, TResponse> resolve(Class<TRequest> requestType) {
        return handlers.get(requestType);
    }
}
