package org.nikola.velemir.poshtar.opt.processor;

import com.google.auto.service.AutoService;
import org.nikola.velemir.poshtar.opt.rules.AmbiguityRule;
import org.nikola.velemir.poshtar.opt.rules.NoInjectionRule;
import org.nikola.velemir.poshtar.opt.rules.Rule;
import org.nikola.velemir.poshtar.opt.rules.RuleContext;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;

@AutoService(Processor.class)
@SupportedAnnotationTypes("*")
@SupportedSourceVersion(SourceVersion.RELEASE_21)
public class PoshtarGuardProcessor extends AbstractProcessor {
    private final List<Rule> rules = List.of(new AmbiguityRule(), new NoInjectionRule());
    private Properties registry;
    private static final String REGISTRY_RESOURCE = "META-INF/poshtar-handlers.properties";

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (roundEnv.processingOver()) {
            if (this.registry != null) writeRegistry(this.registry);
            return false;
        }

        if (registry == null) registry = loadExistingRegistry();
        RuleContext ctx = new RuleContext(processingEnv, registry);

        TypeElement handlerAnnot = processingEnv.getElementUtils()
                .getTypeElement("org.nikola.velemir.poshtar.core.annotations.Handler");

        if (handlerAnnot != null) {
            roundEnv.getElementsAnnotatedWith(handlerAnnot).stream()
                    .filter(e -> e.getKind() == ElementKind.CLASS)
                    .map(e -> (TypeElement) e)
                    .forEach(handler -> rules.forEach(rule -> rule.validate(handler, ctx)));
        }

        for (Rule rule : rules) {
            rule.validateRound(roundEnv, ctx);
        }

        return false;
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
}
