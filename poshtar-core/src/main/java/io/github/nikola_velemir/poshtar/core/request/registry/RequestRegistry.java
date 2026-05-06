package io.github.nikola_velemir.poshtar.core.request.registry;

import io.github.nikola_velemir.poshtar.core.pipeline.behaviour.PipelineBehaviour;
import io.github.nikola_velemir.poshtar.core.request.Request;
import io.github.nikola_velemir.poshtar.core.request.RequestInvocationChain;
import io.github.nikola_velemir.poshtar.core.request.handler.RequestHandler;

import java.util.List;

/**
 * Interface representing a request registry, where all request type to handler type mappings will be stored.
 * <p>
 * Implementations of this interface serve two purposes:
 * </p>
 * <ul>
 *     <li>
 *         <b>Resolution:</b> Resolving the appropriate {@link RequestInvocationChain} for a given request type,
 *         allowing the mediator to execute the full processing pipeline.
 *     </li>
 *     <li>
 *         <b>Registration:</b> Binding a specific {@link Request} type to its {@link RequestHandler}
 *         and an ordered list of {@link PipelineBehaviour} components.
 *     </li>
 * </ul>
 *
 * @author Nikola Velemir
 * @version ${project.version}
 * @since 1.0.0
 */
public interface RequestRegistry {
    /**
     * Resolves the invocation chain for a specific request type.
     *
     * @param requestType Class literal of the request to resolve.
     * @param <TRequest>  The type of the request being handled.
     * @param <TResponse> The type of the response returned by handling a request.
     * @return A {@link RequestInvocationChain} that supports the request type, containing wrapping behaviors and the final handler.
     * @throws RuntimeException if no handler is registered to the given request type.
     */
    <TRequest extends Request<TResponse>, TResponse> RequestInvocationChain<TRequest, TResponse> resolve(Class<TRequest> requestType);

    /**
     * Registers a request type by binding to a handler and a set of pipeline behaviors.
     *
     * @param requestType   Class literal of the request to register.
     * @param rawHandler    Handler that will handler a request.
     * @param rawBehaviours A list of behaviors that support the request type.
     * @param <TRequest>    The type of the request.
     * @param <TResponse>   The type of the response.
     */
    <TRequest extends Request<TResponse>, TResponse> void register(
            Class<TRequest> requestType,
            RequestHandler<TRequest, TResponse> rawHandler,
            List<PipelineBehaviour<?, ?>> rawBehaviours);

}
