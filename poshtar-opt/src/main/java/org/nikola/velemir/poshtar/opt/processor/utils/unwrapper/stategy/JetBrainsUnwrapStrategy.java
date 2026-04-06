package org.nikola.velemir.poshtar.opt.processor.utils.unwrapper.stategy;

public class JetBrainsUnwrapStrategy implements UnwrapStrategy {
    private static final String API_WRAPPERS = "org.jetbrains.jps.javac.APIWrappers";

    @Override
    public boolean supports(Object wrapper) {
        try {
            wrapper.getClass().getClassLoader().loadClass(API_WRAPPERS);
            return true;
        } catch (Throwable ignored) {
            return false;
        }
    }

    @Override
    public <T> T unwrap(Class<? extends T> iface, T wrapper) {
        try {

            final Class<?> apiWrappers = wrapper.getClass().getClassLoader()
                    .loadClass("org.jetbrains.jps.javac.APIWrappers");
            final java.lang.reflect.Method unwrapMethod = apiWrappers
                    .getDeclaredMethod("unwrap", Class.class, Object.class);
            return iface.cast(unwrapMethod.invoke(null, iface, wrapper));
        } catch (Throwable ignored) {
            return wrapper;
        }
    }
}
