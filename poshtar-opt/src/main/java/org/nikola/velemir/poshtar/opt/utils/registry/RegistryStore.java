package org.nikola.velemir.poshtar.opt.utils.registry;

import javax.annotation.processing.ProcessingEnvironment;
import javax.tools.Diagnostic;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

public class RegistryStore {
    private static final String REGISTRY_RESOURCE = "META-INF/poshtar-handlers.properties";

    public static Properties loadExistingRegistry(ProcessingEnvironment processingEnv) {

        Properties props = new Properties();
        try {
            FileObject resource = processingEnv.getFiler().getResource(
                    StandardLocation.CLASS_OUTPUT,
                    "",
                    REGISTRY_RESOURCE
            );
            try (InputStream in = resource.openInputStream()) {
                props.load(in);
            }
        } catch (IOException e) {
            // File doesn't exist yet
        }
        return props;
    }

    public static void writeRegistry(ProcessingEnvironment processingEnv, Properties registry) {
        try {
            FileObject resource = processingEnv.getFiler().createResource(
                    StandardLocation.CLASS_OUTPUT,
                    "",
                    REGISTRY_RESOURCE
            );
            try (OutputStream out = resource.openOutputStream()) {
                registry.store(out, "PoshtaR handler registry — do not edit manually");
            }
        } catch (IOException e) {
            processingEnv.getMessager().printMessage(
                    Diagnostic.Kind.WARNING,
                    "PoshtaR: could not write handler registry: " + e.getMessage()
            );
        }
    }
}
