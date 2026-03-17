package org.nikola.velemir.poshtar.core.exceptions;

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
