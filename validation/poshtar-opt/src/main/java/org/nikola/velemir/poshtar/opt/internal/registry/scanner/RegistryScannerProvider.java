package org.nikola.velemir.poshtar.opt.internal.registry.scanner;

public class RegistryScannerProvider {
    public static RegistryScanner provideScanner() {
        return new RegistryScannerImpl();
    }
}
