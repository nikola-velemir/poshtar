package org.example.core.request.registry;

import org.example.core.pipeline.behaviour.IPipelineBehaviour;
import org.example.core.request.IRequest;
import org.example.core.request.RequestInvocationChain;
import org.example.core.request.handler.IRequestHandler;

import java.util.List;

public interface IRequestRegistry {
    <TRequest extends IRequest<TResponse>, TResponse> RequestInvocationChain<TRequest,TResponse> resolve(Class<TRequest> requestType);

    void register(
            Class<?> requestType,
            IRequestHandler<?, ?> rawHandler,
            List<IPipelineBehaviour<?, ?>> rawBehaviours);

}
