package io.github.nikola_velemir.poshtar.validator.internal.rules.noInjection;

public class InjectionBypassCheckerProvider {
    public static InjectionBypassChecker provide(){
        return new InjectionBypassCheckerImpl();
    }
}
