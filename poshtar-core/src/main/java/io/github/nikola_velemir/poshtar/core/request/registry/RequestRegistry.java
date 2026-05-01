package io.github.nikola_velemir.poshtar.core.request.registry;

import io.github.nikola_velemir.poshtar.core.pipeline.behaviour.PipelineBehaviour;
import io.github.nikola_velemir.poshtar.core.request.Request;
import io.github.nikola_velemir.poshtar.core.request.RequestInvocationChain;
import io.github.nikola_velemir.poshtar.core.request.handler.RequestHandler;

import java.util.List;

public interface RequestRegistry {
    <TRequest extends Request<TResponse>, TResponse> RequestInvocationChain<TRequest,TResponse> resolve(Class<TRequest> requestType);

     <TRequest extends Request<TResponse>, TResponse> void register(
            Class<TRequest> requestType,
            RequestHandler<TRequest, TResponse> rawHandler,
            List<PipelineBehaviour<?, ?>> rawBehaviours);

}
