package io.github.nikola_velemir.poshtar.core.exceptions;

import java.util.List;
/**
 * Exception thrown when multiple handlers are discovered or registered for a single {@link io.github.nikola_velemir.poshtar.core.request.Request} type.
 * <p>
 * The Poshtar requires a strict one-to-one mapping for requests to ensure
 * deterministic behavior.
 * </p>
 *
 * @author Nikola Velemir
 * @version ${project.version}
 * @since 1.0.0
 */
public class AmbiguousHandlerException extends PoshtarException {
    /**
     * Constructs a new exception detailing the conflicting handlers for a specific request type.
     *
     * @param requestClass The class of the request that has multiple handlers assigned.
     * @param handlers     A list of the handler classes that were found to be in conflict.
     */
    public AmbiguousHandlerException(Class<?> requestClass, List<Class<?>> handlers) {
        super(
                String.format("There are multiple handlers registered for type [%s]: %s",
                        requestClass.getSimpleName(), handlers.toString())
        );
    }
}
