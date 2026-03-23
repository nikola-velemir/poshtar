package org.nikola.velemir.poshtar.opt.processor;

import com.google.auto.service.AutoService;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;

@AutoService(Processor.class)
@SupportedAnnotationTypes("org.nikola.velemir.poshtar.core.annotations.Handler")
@SupportedSourceVersion(SourceVersion.RELEASE_21)
public class AmbiguityProcessor extends AbstractProcessor {

    private static final String REGISTRY_RESOURCE = "META-INF/poshtar-handlers.properties";

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (roundEnv.processingOver()) return false;

        Properties registry = loadExistingRegistry();

        boolean hasError = false;

        for (TypeElement annotation : annotations) {
            for (Element element : roundEnv.getElementsAnnotatedWith(annotation)) {
                if (element.getKind() != ElementKind.CLASS) continue;

                TypeElement handlerElement = (TypeElement) element;
                String handlerClassName = handlerElement.getQualifiedName().toString();
                String requestClassName = extractRequestType(handlerElement);

                if (requestClassName == null) continue;

                String existingHandler = registry.getProperty(requestClassName);

                if (existingHandler != null && !existingHandler.equals(handlerClassName)) {
                    processingEnv.getMessager().printMessage(
                            Diagnostic.Kind.ERROR,
                            String.format(
                                    "PoshtaR: Ambiguous handlers for '%s':%n" +
                                            "  already registered: '%s'%n" +
                                            "  conflict with:      '%s'",
                                    requestClassName, existingHandler, handlerClassName
                            ),
                            handlerElement
                    );
                    hasError = true;
                } else {
                    registry.setProperty(requestClassName, handlerClassName);
                }
            }
        }

        if (!hasError) {
            writeRegistry(registry);
        }

        return true;
    }
    private Properties loadExistingRegistry() {
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
            // File doesn't exist yet — first pass, this is expected
        }
        return props;
    }

    private void writeRegistry(Properties registry) {
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
    private String extractRequestType(TypeElement handlerElement) {
        for (TypeMirror interfaceMirror : handlerElement.getInterfaces()) {
            if (interfaceMirror.getKind() != TypeKind.DECLARED) continue;

            DeclaredType declaredInterface = (DeclaredType) interfaceMirror;
            Element interfaceElement = declaredInterface.asElement();

            if (!interfaceElement.getSimpleName().contentEquals("RequestHandler")) continue;

            List<? extends TypeMirror> typeArgs = declaredInterface.getTypeArguments();
            if (!typeArgs.isEmpty()) {
                return typeArgs.get(0).toString();
            }
        }
        return null;
    }
}