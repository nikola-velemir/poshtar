package org.nikola.velemir.poshtar.core.request.registry;

import org.nikola.velemir.poshtar.core.pipeline.behaviour.PipelineBehaviour;
import org.nikola.velemir.poshtar.core.request.Request;
import org.nikola.velemir.poshtar.core.request.RequestInvocationChain;
import org.nikola.velemir.poshtar.core.request.handler.RequestHandler;

import java.util.List;

public interface RequestRegistry {
    <TRequest extends Request<TResponse>, TResponse> RequestInvocationChain<TRequest,TResponse> resolve(Class<TRequest> requestType);

     <TRequest extends Request<TResponse>, TResponse> void register(
            Class<TRequest> requestType,
            RequestHandler<TRequest, TResponse> rawHandler,
            List<PipelineBehaviour<?, ?>> rawBehaviours);

}
