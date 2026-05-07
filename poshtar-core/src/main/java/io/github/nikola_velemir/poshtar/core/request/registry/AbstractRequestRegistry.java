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

/**
 * Base class implementing {@link RequestRegistry}, modeling standard behavior of registering and resolving request types.
 *
 * @author Nikola Velemir
 * @version ${project.version}
 * @since 1.0.0
 */
public abstract class AbstractRequestRegistry implements RequestRegistry {

    /**
     * Map containing request to chain mappings, which are later searched for invocation chain resolution.
     */
    protected final Map<Class<?>, RequestInvocationChain<?, ?>> handlerMappings = new HashMap<>();

    /**
     * Implementation of the {@link RequestRegistry}'s {@code resolve} method. Throws {@link HandlerNotFoundException} if resolution fails.
     *
     * @param requestType Class literal of the request to resolve.
     * @param <TRequest>  The type of the request being handled.
     * @param <TResponse> The type of the response returned by handling a request.
     * @return A {@link RequestInvocationChain} that supports the request type, containing wrapping behaviors and the final handler.
     * @throws HandlerNotFoundException if no handler is registered to the given request type.
     */
    @SuppressWarnings("unchecked")
    @Override
    public <TRequest extends Request<TResponse>, TResponse> RequestInvocationChain<TRequest, TResponse> resolve(Class<TRequest> requestType) {
        RequestInvocationChain<TRequest, TResponse> requestChain = (RequestInvocationChain<TRequest, TResponse>) handlerMappings.get(requestType);
        if (requestChain == null)
            throw new HandlerNotFoundException(requestType);
        return requestChain;
    }

    /**
     * Implementation of the {@link RequestRegistry}'s {@code register} method.
     * Throws {@link AmbiguousHandlerException} if there are is a handler already registered to the given request type.
     *
     * @param requestType   Class literal of the request to register.
     * @param handler       Handler that will handler a request.
     * @param rawBehaviours A list of behaviors that support the request type.
     * @param <TRequest>    The type of the request.
     * @param <TResponse>   The type of the response.
     * @throws AmbiguousHandlerException if handler is already registered to the request type.
     */
    @Override
    public <TRequest extends Request<TResponse>, TResponse> void register(Class<TRequest> requestType, RequestHandler<TRequest, TResponse> handler, List<PipelineBehaviour<?, ?>> rawBehaviours) {
        var builtPipeline = PipelineFactory.create(handler, rawBehaviours);
        var putResult =
                handlerMappings.putIfAbsent(requestType, builtPipeline);
        if (putResult != null)
            throw new AmbiguousHandlerException(requestType, List.of(handler.getClass()));
    }

    /**
     * Method that filters the behaviors by their generic constraint.
     * Resulting list of behaviors supports {@param requestType}.
     *
     * @param allBehaviours Behaviors provided for filtering.
     * @param requestType   Class literal of the request.
     * @return List of behaviors that support the request type.
     */
    protected List<PipelineBehaviour<?, ?>> filterBehaviours(
            List<PipelineBehaviour<?, ?>> allBehaviours, Class<?> requestType) {

        return allBehaviours.stream()
                .filter(b -> supportsRequest(b, requestType))
                .toList();
    }

    /**
     * Method overrides are to check if the provided behavior supports the request type thru generic constraints.
     *
     * @param behaviour   Behavior that is being tested whether it supports the {@param requestType}
     * @param requestType Class literal of the request type.
     * @return {@code true} if the behavior supports the request type, otherwise {@code false}
     */
    protected abstract boolean supportsRequest(PipelineBehaviour<?, ?> behaviour, Class<?> requestType);
}
