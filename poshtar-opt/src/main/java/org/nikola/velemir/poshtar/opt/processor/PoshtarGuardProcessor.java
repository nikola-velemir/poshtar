package org.nikola.velemir.poshtar.opt.processor;

import com.google.auto.service.AutoService;
import com.sun.source.util.Trees;
import org.nikola.velemir.poshtar.core.annotations.Handler;
import org.nikola.velemir.poshtar.opt.rules.ambiguity.AmbiguityRule;
import org.nikola.velemir.poshtar.opt.rules.injection.HandlerNoInjectionRule;
import org.nikola.velemir.poshtar.opt.rules.Rule;
import org.nikola.velemir.poshtar.opt.rules.RuleContext;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
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

@AutoService(Processor.class)
@SupportedAnnotationTypes("*")
@SupportedSourceVersion(SourceVersion.RELEASE_21)
public class PoshtarGuardProcessor extends AbstractProcessor {
    public static final String HANDLER_ANNOTATION_NAME = Handler.class.getName();

    private final List<Rule> rules = List.of(
            new AmbiguityRule(),
            new HandlerNoInjectionRule()
    );
    private Properties registry;
    private static final String REGISTRY_RESOURCE = "META-INF/poshtar-handlers.properties";
    private Trees trees;

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (roundEnv.processingOver()) {
            if (this.registry != null) writeRegistry(this.registry);
            return false;
        }

        if (registry == null) registry = loadExistingRegistry();
        RuleContext ctx = new RuleContext(processingEnv, trees, registry);

        TypeElement handlerAnnot = processingEnv.getElementUtils()
                .getTypeElement(HANDLER_ANNOTATION_NAME);

        validatePerElement(roundEnv, handlerAnnot, ctx);
        validatePerRound(roundEnv, ctx);

        return false;
    }

    private void validatePerRound(RoundEnvironment roundEnv, RuleContext ctx) {
        for (Rule rule : rules) {
            rule.validateRound(roundEnv, ctx);
        }
    }

    private void validatePerElement(RoundEnvironment roundEnv, TypeElement handlerAnnot, RuleContext ctx) {
        if (handlerAnnot != null) {
            roundEnv.getElementsAnnotatedWith(handlerAnnot).stream()
                    .filter(e -> e.getKind() == ElementKind.CLASS)
                    .map(e -> (TypeElement) e)
                    .forEach(handler -> rules.forEach(rule -> rule.validate(handler, ctx)));
        }
    }

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        ProcessingEnvironment unwrapped = jbUnwrap(ProcessingEnvironment.class, processingEnv);
        this.trees = Trees.instance(unwrapped);
    }

    private static <T> T jbUnwrap(Class<? extends T> iface, T wrapper) {
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
