package io.github.nikola_velemir.poshtar.core.exceptions;
/**
 * Base exception class for all errors occurring within the Poshtar library.
 * <p>
 * This is a {@link RuntimeException}, meaning it does not require explicit
 * catch blocks, but it serves as the root for more specific exceptions
 * (e.g., registration or dispatching errors). All messages are automatically
 * prefixed with {@code [PoshtaR]} for easier identification in application logs.
 * </p>
 *
 * @author Nikola Velemir
 * @version ${project.version}
 * @since 1.0.0
 */
public class PoshtarException extends RuntimeException {

    /**
     * Creates an exception instance, with the leading tag of the library name.
     * @param message Custom excpetion message.
     */
    public PoshtarException(String message) {
        super("[PoshtaR] " + message);
    }
    /**
     * Creates an exception instance, with the leading tag of the library name.
     * @param message Custom exception message.
     * @param cause Throwable that represents a cause of the error.
     */
    public PoshtarException(String message, Throwable cause) {
        super("[PoshtaR] " + message, cause);
    }
}