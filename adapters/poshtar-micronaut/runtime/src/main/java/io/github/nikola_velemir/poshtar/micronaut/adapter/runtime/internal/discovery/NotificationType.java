package io.github.nikola_velemir.poshtar.micronaut.adapter.runtime.internal.discovery;

import io.micronaut.core.annotation.Internal;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Internal
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface NotificationType {
    Class<?> value();
}
