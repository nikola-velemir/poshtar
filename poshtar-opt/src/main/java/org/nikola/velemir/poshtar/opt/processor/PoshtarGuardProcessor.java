package org.nikola.velemir.poshtar.opt.processor;

import com.google.auto.service.AutoService;
import com.sun.source.util.Trees;
import org.nikola.velemir.poshtar.core.annotations.Behaviour;
import org.nikola.velemir.poshtar.core.annotations.Handler;
import org.nikola.velemir.poshtar.core.pipeline.behaviour.PipelineBehaviour;
import org.nikola.velemir.poshtar.core.request.handler.RequestHandler;
import org.nikola.velemir.poshtar.opt.rules.ambiguity.AmbiguityRule;
import org.nikola.velemir.poshtar.opt.rules.deadPipeline.DeadPipelineRule;
import org.nikola.velemir.poshtar.opt.rules.injection.BehaviourNoInjectionRule;
import org.nikola.velemir.poshtar.opt.rules.injection.HandlerNoInjectionRule;
import org.nikola.velemir.poshtar.opt.rules.Rule;
import org.nikola.velemir.poshtar.opt.rules.RuleContext;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
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
import java.util.List;
import java.util.Properties;
import java.util.Set;

@AutoService(Processor.class)
@SupportedAnnotationTypes("*")
@SupportedSourceVersion(SourceVersion.RELEASE_21)
public class PoshtarGuardProcessor extends AbstractProcessor {
    private static final String HANDLER_ANNOTATION_NAME = Handler.class.getName();
    private static final String BEHAVIOUR_ANNOTATION_NAME = Behaviour.class.getName();
    private static final String REQUEST_HANDLER_INTERFACE_NAME = RequestHandler.class.getName();

    private final List<Rule> rules = List.of(
            new AmbiguityRule(),
            new HandlerNoInjectionRule(),
            new BehaviourNoInjectionRule(),
           new DeadPipelineRule()
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

        preprocessRegistry(roundEnv, ctx);

        validateRules(roundEnv, ctx);

        return false;
    }

    private void preprocessRegistry(RoundEnvironment roundEnv, RuleContext ctx) {
        var elements = processingEnv.getElementUtils();

        TypeElement handlerAnnot = elements.getTypeElement(HANDLER_ANNOTATION_NAME);
        if (handlerAnnot != null) {
            preprocessHandlers(roundEnv, ctx, handlerAnnot);
        }

        TypeElement behaviourAnnot = elements.getTypeElement(BEHAVIOUR_ANNOTATION_NAME);
        if (behaviourAnnot != null) {
            processBehaviours(roundEnv, ctx, behaviourAnnot);
        }
    }

    private static void processBehaviours(RoundEnvironment roundEnv, RuleContext ctx, TypeElement behaviourAnnot) {
        roundEnv.getElementsAnnotatedWith(behaviourAnnot).stream()
                .filter(e -> e.getKind() == ElementKind.CLASS)
                .map(e -> (TypeElement) e)
                .forEach(b -> ctx.registerHandler(b.getQualifiedName().toString(), "BEHAVIOUR"));
    }

    private void preprocessHandlers(RoundEnvironment roundEnv, RuleContext ctx, TypeElement handlerAnnot) {
        roundEnv.getElementsAnnotatedWith(handlerAnnot).stream()
                .filter(e -> e.getKind() == ElementKind.CLASS)
                .map(e -> (TypeElement) e)
                .forEach(h -> {
                    String requestType = this.extractRequestType(h, ctx);
                    // The critical guard: Properties/Hashtable will crash on null values
                    if (requestType != null) {
                        ctx.registerHandler(h.getQualifiedName().toString(), requestType);
                    }
                });
    }

    private void validateRules(RoundEnvironment roundEnv, RuleContext ctx) {
        for (Rule rule : rules) {
            rule.validate(roundEnv, ctx);
        }
    }

    private String extractRequestType(TypeElement handler, RuleContext ctx) {
        var typeUtils = ctx.env.getTypeUtils();
        var elementUtils = ctx.env.getElementUtils();

        TypeElement reqHandlerInterface = elementUtils.getTypeElement(REQUEST_HANDLER_INTERFACE_NAME);
        if (reqHandlerInterface == null) return null;

        TypeMirror erasedReqHandler = typeUtils.erasure(reqHandlerInterface.asType());

        for (TypeMirror iface : handler.getInterfaces()) {
            if (typeUtils.isAssignable(typeUtils.erasure(iface), erasedReqHandler)) {
                if (iface instanceof DeclaredType declared) {
                    List<? extends TypeMirror> typeArgs = declared.getTypeArguments();
                    if (typeArgs.isEmpty()) continue;

                    TypeMirror requestType = typeArgs.getFirst();

                    if (requestType.getKind() == TypeKind.ERROR) {
                        ctx.env.getMessager().printMessage(
                                Diagnostic.Kind.ERROR,
                                "PoshtaR: Cannot resolve request type for handler " + handler.getSimpleName() +
                                        ". Ensure the Request class is imported and compiles.",
                                handler
                        );
                        return null;
                    }
                    return typeUtils.erasure(requestType).toString();
                }
            }
        }
        return null;
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
