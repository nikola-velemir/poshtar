package io.github.nikola_velemir.poshtar.core.exceptions;

import java.util.List;

public class AmbiguousHandlerException extends PoshtarException {
    public AmbiguousHandlerException(Class<?> requestClass, List<Class<?>> handlers) {
        super(
                String.format("There are multiple handlers registered for type [%s]: %s",
                        requestClass.getSimpleName(), handlers.toString())
        );
    }
}
