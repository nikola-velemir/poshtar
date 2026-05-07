package io.github.nikola_velemir.poshtar.opt.internal.unwrapper.stategy;

/**
 * Defines a strategy for extracting compiler implementations from IDE proxies.
 * <p>
 * Implementations of this interface handle the vendor-specific logic required to
 * "unwrap" objects like the {@link javax.annotation.processing.ProcessingEnvironment}.
 * This is necessary because IDEs often wrap these objects in proxies that hide
 * internal APIs (such as the {@code com.sun.source.util.Trees} instance).
 * </p>
 *
 * @author Nikola Velemir
 * @version ${project.version}
 * @see io.github.nikola_velemir.poshtar.opt.internal.unwrapper.IdeUnwrapper
 * @since 1.0.0
 */
public interface UnwrapStrategy {
    /**
     * Determines if this strategy is capable of unwrapping the provided object.
     *
     * @param wrapper The object to check for compatibility.
     * @return {@code true} if this strategy can unwrap the object; {@code false} otherwise.
     */
    boolean supports(Object wrapper);

    /**
     * Unwraps the provided object to reveal the requested interface or implementation.
     *
     * @param <T>     The target type to unwrap to.
     * @param iface   The class literal of the target type.
     * @param wrapper The proxied object to be unwrapped.
     * @return The raw, underlying implementation cast to the requested type.
     * @throws ClassCastException if the unwrapped object cannot be cast to {@code T}.
     */
    <T> T unwrap(Class<? extends T> iface, T wrapper);

}
