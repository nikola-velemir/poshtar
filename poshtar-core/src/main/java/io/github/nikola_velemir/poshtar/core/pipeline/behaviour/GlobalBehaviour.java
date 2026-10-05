package io.github.nikola_velemir.poshtar.core.pipeline.behaviour;

import io.github.nikola_velemir.poshtar.core.pipeline.delegate.RequestDelegate;
import io.github.nikola_velemir.poshtar.core.request.Request;
import jakarta.annotation.Nonnull;
/**
 * Defines a cross-cutting concern that can be executed before or after a request is handled.
 * <p>
 * Implementations of this interface is a middleware in the request processing pipeline.
 * </p>
 *
 * <p>This class represents a global behaviour. In other words, request regardless of specific type is going to be caught by implementor of this interface.</p>
 * @author Nikola Velemir
 * @version ${revision}
 * @see io.github.nikola_velemir.poshtar.core.pipeline.behaviour.PipelineBehaviour
 * @since 1.2.0
 */
public interface GlobalBehaviour extends PipelineBehaviour<Request<Object>, Object> {
    @Override
    @Nonnull
    Object handle(@Nonnull Request<Object> request, @Nonnull RequestDelegate<Request<Object>, Object> requestDelegate);
}