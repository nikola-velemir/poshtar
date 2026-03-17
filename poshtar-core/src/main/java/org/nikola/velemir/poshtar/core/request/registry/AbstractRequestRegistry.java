package org.nikola.velemir.poshtar.core.request.registry;

import jdk.jshell.spi.ExecutionControl;
import org.nikola.velemir.poshtar.core.exceptions.AmbiguousHandlerException;
import org.nikola.velemir.poshtar.core.exceptions.HandlerNotFoundException;
import org.nikola.velemir.poshtar.core.pipeline.behaviour.IPipelineBehaviour;
import org.nikola.velemir.poshtar.core.pipeline.builder.PipelineBuilder;
import org.nikola.velemir.poshtar.core.request.IRequest;
import org.nikola.velemir.poshtar.core.request.RequestInvocationChain;
import org.nikola.velemir.poshtar.core.request.handler.IRequestHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractRequestRegistry implements IRequestRegistry {

    protected final Map<Class<?>, RequestInvocationChain<?, ?>> handlerMappings = new HashMap<>();

    @SuppressWarnings("unchecked")
    @Override
    public <TRequest extends IRequest<TResponse>, TResponse> RequestInvocationChain<TRequest, TResponse> resolve(Class<TRequest> requestType) {
        RequestInvocationChain<TRequest, TResponse> requestChain = (RequestInvocationChain<TRequest, TResponse>) handlerMappings.get(requestType);
        if (requestChain == null)
            throw new HandlerNotFoundException(requestType);
        return requestChain;
    }


    @Override
    public <TRequest extends IRequest<TResponse>, TResponse> void register(Class<TRequest> requestType, IRequestHandler<TRequest, TResponse> handler, List<IPipelineBehaviour<?, ?>> rawBehaviours) {
        var builtPipeline = PipelineBuilder.build(handler, rawBehaviours);
        var putResult =
                handlerMappings.putIfAbsent(requestType, builtPipeline);
        if (putResult != null)
            throw new AmbiguousHandlerException(requestType, List.of(handler.getClass()));
    }

    protected List<IPipelineBehaviour<?, ?>> filterBehaviours(
            List<IPipelineBehaviour<?, ?>> allBehaviours, Class<?> requestType) {

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

    protected boolean supportsRequest(IPipelineBehaviour<?, ?> ignoredBehaviour, Class<?> ignoredRequestType) throws ExecutionControl.NotImplementedException {
        throw new ExecutionControl.NotImplementedException("Inheriting class must override this method");
    }
}
