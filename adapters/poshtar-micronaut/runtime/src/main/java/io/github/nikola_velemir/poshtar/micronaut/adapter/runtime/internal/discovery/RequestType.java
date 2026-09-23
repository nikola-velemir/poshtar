package io.github.nikola_velemir.poshtar.micronaut.adapter.runtime.internal.discovery;

import io.micronaut.core.annotation.Internal;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Compiler-written marker annotation recording the resolved {@code Request}
 * generic type argument of a
 * {@link io.github.nikola_velemir.poshtar.core.request.handler.RequestHandler}.
 * <p>
 * Micronaut merges the annotation metadata of a bean onto the
 * {@code BeanDefinition} of any proxy it creates for that bean, this
 * annotation is reliably present on {@code BeanDefinition<RequestHandler>}
 * at runtime regardless of whether the handler ends up proxied.
 * <p>
 * Never applied by hand - it is purely compiler-generated metadata.
 */
@Internal
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface RequestType {
    Class<?> value();
}

