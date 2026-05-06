package io.github.nikola_velemir.poshtar.core.types;

import io.github.nikola_velemir.poshtar.core.request.Request;

/**
 * Represents a type that is used to indicate that a {@link Request}
 * does not return a meaningful value.
 *
 * <p>
 * This class is equivalent to {@code void} or {@link Void}.
 * </p>
 *
 * @author Nikola Velemir
 * @version ${project.version}
 * @since 1.0.0
 */
public final class Unit {
    /**
     * The single shared instance of the {@code Unit} type.
     */
    public static final Unit Value = new Unit();

    private Unit() {
    }
}
