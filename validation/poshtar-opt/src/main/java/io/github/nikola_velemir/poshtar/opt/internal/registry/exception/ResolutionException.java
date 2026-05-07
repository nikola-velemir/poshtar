package io.github.nikola_velemir.poshtar.opt.internal.registry.exception;

/**
 * Exception to be thrown if component resolution fails during registry scanning process.
 *
 * @author Nikola Velemir
 * @version ${project.version}
 * @since 1.0.0
 */
public class ResolutionException extends RuntimeException {


    public ResolutionException(String message) {
        super(message);
    }
}
