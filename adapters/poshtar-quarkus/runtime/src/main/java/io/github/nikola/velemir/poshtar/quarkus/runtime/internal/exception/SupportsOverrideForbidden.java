package io.github.nikola.velemir.poshtar.quarkus.runtime.internal.exception;

import io.github.nikola_velemir.poshtar.core.exceptions.PoshtarException;

public class SupportsOverrideForbidden extends PoshtarException {
    public SupportsOverrideForbidden(String message) {
        super(message);
    }

    public SupportsOverrideForbidden() {
        super("supportsRequest should never be called at runtime");
    }
}
