package org.example.core.request.registry;

import org.example.core.pipeline.behaviour.IPipelineBehaviour;
import org.example.core.request.IRequest;
import org.example.core.request.RequestChain;
import org.example.core.request.handler.IRequestHandler;

import java.util.List;

public interface IRequestRegistry {
    <TRequest extends IRequest<TResponse>, TResponse> RequestChain<TRequest,TResponse> resolve(Class<TRequest> requestType);
    <TReq extends IRequest<TRes>, TRes> RequestChain<TReq, TRes> buildChain(
            IRequestHandler<?, ?> rawHandler,
            List<IPipelineBehaviour<?, ?>> rawBehaviours);
    void register(
            Class<?> requestType,
            IRequestHandler<?, ?> rawHandler,
            List<IPipelineBehaviour<?, ?>> rawBehaviours);

}
