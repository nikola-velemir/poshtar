package org.example.core.request.registry;

import jdk.jshell.spi.ExecutionControl;
import org.example.core.exceptions.AmbiguousHandlerException;
import org.example.core.exceptions.HandlerNotFoundException;
import org.example.core.pipeline.behaviour.IPipelineBehaviour;
import org.example.core.pipeline.delegate.RequestDelegate;
import org.example.core.request.IRequest;
import org.example.core.request.RequestChain;
import org.example.core.request.handler.IRequestHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractRequestRegistry implements IRequestRegistry {

    protected final Map<Class<?>, RequestChain<?, ?>> handlerMappings = new HashMap<>();

    @SuppressWarnings("unchecked")
    @Override
    public <TRequest extends IRequest<TResponse>, TResponse> RequestChain<TRequest, TResponse> resolve(Class<TRequest> requestType) {
        RequestChain<TRequest, TResponse> requestChain = (RequestChain<TRequest, TResponse>) handlerMappings.get(requestType);
        if (requestChain == null)
            throw new HandlerNotFoundException(requestType);
        return requestChain;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <TRequest extends IRequest<TResponse>, TResponse> RequestChain<TRequest, TResponse> buildChain(IRequestHandler<?, ?> rawHandler, List<IPipelineBehaviour<?, ?>> rawBehaviours) {
        IRequestHandler<TRequest, TResponse> handler =
                (IRequestHandler<TRequest, TResponse>) rawHandler;

        List<IPipelineBehaviour<TRequest, TResponse>> behaviours = rawBehaviours.stream()
                .map(b -> (IPipelineBehaviour<TRequest, TResponse>) b)
                .toList();

        return (request) -> {
            RequestDelegate<TResponse> next = () -> handler.handle(request);

            for (int i = behaviours.size() - 1; i >= 0; i--) {
                IPipelineBehaviour<TRequest, TResponse> behaviour = behaviours.get(i);
                RequestDelegate<TResponse> currentNext = next;
                next = () -> behaviour.handle(request, currentNext);
            }

            return next.handle();
        };
    }

    @Override
    public void register(Class<?> requestType, IRequestHandler<?, ?> rawHandler, List<IPipelineBehaviour<?, ?>> rawBehaviours) {
        var putResult =
                handlerMappings.putIfAbsent(requestType, buildChain(rawHandler, rawBehaviours));
        if (putResult != null)
            throw new AmbiguousHandlerException(requestType, List.of(rawHandler.getClass()));
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
