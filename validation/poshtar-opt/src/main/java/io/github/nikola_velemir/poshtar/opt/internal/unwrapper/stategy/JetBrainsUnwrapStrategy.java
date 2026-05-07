package io.github.nikola_velemir.poshtar.opt.internal.unwrapper.stategy;
/**
 * Unwrapping strategy specifically designed for the JetBrains (IntelliJ IDEA) build environment.
 * <p>
 * This strategy detects the
 * presence of IntelliJ's {@code APIWrappers} utility and uses reflection to extract the
 * underlying compiler implementation.
 * </p>
 *
 * @author Nikola Velemir
 * @version ${project.version}
 * @since 1.0.0
 */
class JetBrainsUnwrapStrategy implements UnwrapStrategy {
    private static final String API_WRAPPERS = "org.jetbrains.jps.javac.APIWrappers";
    /**
     * Checks if the JetBrains JPS API wrappers are available in the current class loader.
     *
     * @param wrapper The object to check.
     * @return {@code true} if the IntelliJ wrapper API is detected; {@code false} otherwise.
     */
    @Override
    public boolean supports(Object wrapper) {
        try {
            wrapper.getClass().getClassLoader().loadClass(API_WRAPPERS);
            return true;
        } catch (Throwable ignored) {
            return false;
        }
    }
    /**
     * Invokes the IntelliJ {@code APIWrappers.unwrap} method via reflection to recover
     * the raw compiler component.
     *
     * @param <T>     The target type.
     * @param iface   The class literal of the target type.
     * @param wrapper The proxied object from the JetBrains environment.
     * @return The unwrapped object if successful; otherwise, returns the original wrapper.
     */
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
