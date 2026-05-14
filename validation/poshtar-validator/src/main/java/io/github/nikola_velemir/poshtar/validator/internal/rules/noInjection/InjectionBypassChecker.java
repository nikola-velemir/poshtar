package io.github.nikola_velemir.poshtar.validator.internal.rules.noInjection;

import io.github.nikola_velemir.poshtar.validator.internal.context.ProcessorContext;

import javax.lang.model.element.TypeElement;

public interface InjectionBypassChecker {
    boolean isBypassed(TypeElement clazz, ProcessorContext ctx);

}

