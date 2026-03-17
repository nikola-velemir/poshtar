package org.nikola.velemir.poshtar.core.request.registry;

import org.nikola.velemir.poshtar.core.pipeline.behaviour.IPipelineBehaviour;
import org.nikola.velemir.poshtar.core.request.IRequest;
import org.nikola.velemir.poshtar.core.request.RequestInvocationChain;
import org.nikola.velemir.poshtar.core.request.handler.IRequestHandler;

import java.util.List;

public interface IRequestRegistry {
    <TRequest extends IRequest<TResponse>, TResponse> RequestInvocationChain<TRequest,TResponse> resolve(Class<TRequest> requestType);

     <TRequest extends IRequest<TResponse>, TResponse> void register(
            Class<TRequest> requestType,
            IRequestHandler<TRequest, TResponse> rawHandler,
            List<IPipelineBehaviour<?, ?>> rawBehaviours);

}
