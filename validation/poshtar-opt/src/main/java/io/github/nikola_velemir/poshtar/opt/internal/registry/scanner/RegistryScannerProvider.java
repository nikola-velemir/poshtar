package io.github.nikola_velemir.poshtar.opt.internal.registry.scanner;

/**
 * Class acts a "injector", that will provide {@link RegistryScanner} implementation,
 * as its concrete implementation is package-private.
 *
 * @author Nikola Velemir
 * @version ${project.version}
 * @since 1.0.0
 */
public class RegistryScannerProvider {
    /**
     * Provides an implementation of {@link RegistryScanner}, used for class path scanning.
     *
     * @return Concrete implementation of {@link RegistryScanner}
     */
    public static RegistryScanner provideScanner() {
        return new RegistryScannerImpl();
    }
}
