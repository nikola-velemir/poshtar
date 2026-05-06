package io.github.nikola_velemir.poshtar.core.exceptions;
/**
 * Exception thrown when the mediator attempts to send a request that has no
 * associated handler registered in the {@link io.github.nikola_velemir.poshtar.core.request.registry.RequestRegistry}.
 * <p>
 * Occurs if a handler class is missing the proper annotations,
 * or if the package containing the handler was not included.
 * </p>
 *
 * @author Nikola Velemir
 * @version ${project.version}
 * @since 1.0.0
 */
public class HandlerNotFoundException extends PoshtarException {
    public HandlerNotFoundException(Class<?> requestClass) {
        super(
                String.format(
                        "No handler found for type: [%s].",
                        requestClass.getSimpleName()
                )
        );
    }
}
