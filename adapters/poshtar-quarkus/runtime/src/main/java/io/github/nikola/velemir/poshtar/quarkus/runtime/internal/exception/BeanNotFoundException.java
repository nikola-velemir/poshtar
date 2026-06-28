package io.github.nikola.velemir.poshtar.quarkus.runtime.internal.exception;

import io.github.nikola_velemir.poshtar.core.exceptions.PoshtarException;

public class BeanNotFoundException extends PoshtarException {

    public BeanNotFoundException(String message) {
        super(message);
    }

    public BeanNotFoundException(Class<?> beanClass) {
        super("No CDI bean for handler: " + beanClass.getName());
    }
}
