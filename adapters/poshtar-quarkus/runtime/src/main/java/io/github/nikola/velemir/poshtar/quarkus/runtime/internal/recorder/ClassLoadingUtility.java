package io.github.nikola.velemir.poshtar.quarkus.runtime.internal.recorder;

/**
 * Class loading utility, used to resolve class FQNs.
 *
 * @author Nikola Velemir
 * @since 1.0.0
 */
final class ClassLoadingUtility {
    private ClassLoadingUtility() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    /**
     * Loads a class by its fully qualified name using the current thread's context class loader.
     *
     * @param name the fully qualified name of the desired class
     * @return the resolved {@link Class} object
     * @throws RuntimeException if the class cannot be located by the class loader
     */
    public static Class<?> loadClass(String name) {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        try {
            return Class.forName(name, true, cl);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Could not load class: " + name, e);
        }
    }
}
