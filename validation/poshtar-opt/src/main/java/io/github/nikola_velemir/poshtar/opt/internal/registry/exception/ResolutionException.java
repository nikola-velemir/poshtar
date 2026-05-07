package io.github.nikola_velemir.poshtar.opt.internal.registry.exception;

/**
 * Exception to be thrown if component resolution fails during registry scanning process.
 *
 * @author Nikola Velemir
 * @version ${project.version}
 * @since 1.0.0
 */
public class ResolutionException extends RuntimeException {


    /**
     * Instantiates a new exception, with custom resolution error message.
     *
     * @param message Text of a custom message.
     */
    public ResolutionException(String message) {
        super(message);
    }
}
