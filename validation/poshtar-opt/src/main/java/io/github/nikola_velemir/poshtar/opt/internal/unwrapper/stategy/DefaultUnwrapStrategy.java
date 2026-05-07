package io.github.nikola_velemir.poshtar.opt.internal.unwrapper.stategy;
/**
 * Unwrapping strategy that is defaulted to if no previous strategies score a match.
 *
 * @author Nikola Velemir
 * @version ${project.version}
 * @since 1.0.0
 */
class DefaultUnwrapStrategy implements UnwrapStrategy {
    @Override
    public boolean supports(Object wrapper) {
        return true;
    }

    @Override
    public <T> T unwrap(Class<? extends T> iface, T wrapper) {
        return wrapper;
    }
}
