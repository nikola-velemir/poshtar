package io.github.nikola_velemir.poshtar.core.request.registry;

import jdk.jshell.spi.ExecutionControl;
import io.github.nikola_velemir.poshtar.core.exceptions.AmbiguousHandlerException;
import io.github.nikola_velemir.poshtar.core.exceptions.HandlerNotFoundException;
import io.github.nikola_velemir.poshtar.core.pipeline.behaviour.PipelineBehaviour;
import io.github.nikola_velemir.poshtar.core.pipeline.factory.PipelineFactory;
import io.github.nikola_velemir.poshtar.core.request.Request;
import io.github.nikola_velemir.poshtar.core.request.RequestInvocationChain;
import io.github.nikola_velemir.poshtar.core.request.handler.RequestHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractRequestRegistry implements RequestRegistry {

    protected final Map<Class<?>, RequestInvocationChain<?, ?>> handlerMappings = new HashMap<>();

    @SuppressWarnings("unchecked")
    @Override
    public <TRequest extends Request<TResponse>, TResponse> RequestInvocationChain<TRequest, TResponse> resolve(Class<TRequest> requestType) {
        RequestInvocationChain<TRequest, TResponse> requestChain = (RequestInvocationChain<TRequest, TResponse>) handlerMappings.get(requestType);
        if (requestChain == null)
            throw new HandlerNotFoundException(requestType);
        return requestChain;
    }


    @Override
    public <TRequest extends Request<TResponse>, TResponse> void register(Class<TRequest> requestType, RequestHandler<TRequest, TResponse> handler, List<PipelineBehaviour<?, ?>> rawBehaviours) {
        var builtPipeline = PipelineFactory.create(handler, rawBehaviours);
        var putResult =
                handlerMappings.putIfAbsent(requestType, builtPipeline);
        if (putResult != null)
            throw new AmbiguousHandlerException(requestType, List.of(handler.getClass()));
    }

    protected List<PipelineBehaviour<?, ?>> filterBehaviours(
            List<PipelineBehaviour<?, ?>> allBehaviours, Class<?> requestType) {

        return allBehaviours.stream()
                .filter(b -> {
                    try {
                        return supportsRequest(b, requestType);
                    } catch (ExecutionControl.NotImplementedException e) {
                        throw new RuntimeException(e);
                    }
                })
                .toList();
    }

    protected boolean supportsRequest(PipelineBehaviour<?, ?> ignoredBehaviour, Class<?> ignoredRequestType) throws ExecutionControl.NotImplementedException {
        throw new ExecutionControl.NotImplementedException("Inheriting class must override this method");
    }
}
