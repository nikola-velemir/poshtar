package io.github.nikola_velemir.poshtar.opt.internal.registry.scanner;

public class RegistryScannerProvider {
    public static RegistryScanner provideScanner() {
        return new RegistryScannerImpl();
    }
}
