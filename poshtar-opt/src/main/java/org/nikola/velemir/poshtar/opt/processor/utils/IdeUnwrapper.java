package org.nikola.velemir.poshtar.opt.processor.utils;

public class IdeUnwrapper {
    public static <T> T jbUnwrap(Class<? extends T> iface, T wrapper) {
        T unwrapped = null;
        try {

            final Class<?> apiWrappers = wrapper.getClass().getClassLoader()
                    .loadClass("org.jetbrains.jps.javac.APIWrappers");
            final java.lang.reflect.Method unwrapMethod = apiWrappers
                    .getDeclaredMethod("unwrap", Class.class, Object.class);
            unwrapped = iface.cast(unwrapMethod.invoke(null, iface, wrapper));
        } catch (Throwable ignored) {
            // Fallback for command-line javac where no wrapper exists
        }
        return unwrapped != null ? unwrapped : wrapper;
    }
}
