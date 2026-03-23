package org.nikola.velemir.poshtar.opt.processor;

import com.google.auto.service.AutoService;
import com.sun.source.tree.NewClassTree;
import com.sun.source.tree.VariableTree;
import com.sun.source.util.TreePathScanner;
import com.sun.source.util.Trees;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

@AutoService(Processor.class)
@SupportedAnnotationTypes("*")
@SupportedSourceVersion(SourceVersion.RELEASE_21)
public class InstantiationProcessor extends AbstractProcessor {

    private static final String HANDLER_ANNOTATION =
            "org.nikola.velemir.poshtar.core.annotations.Handler";
    private static final String MEDIATOR_CLASS =
            "org.nikola.velemir.poshtar.core.Mediator";

    private Trees trees;
    private final Set<String> handlerClassNames = new HashSet<>();

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        trees = Trees.instance(unwrap(ProcessingEnvironment.class, processingEnv));
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (roundEnv.processingOver()) return false;

        TypeElement handlerAnnotation = processingEnv.getElementUtils()
                .getTypeElement(HANDLER_ANNOTATION);

        if (handlerAnnotation != null) {
            for (Element element : roundEnv.getElementsAnnotatedWith(handlerAnnotation)) {
                if (element.getKind() == ElementKind.CLASS) {
                    handlerClassNames.add(
                            ((TypeElement) element).getQualifiedName().toString()
                    );
                }
            }
        }

        for (Element element : roundEnv.getRootElements()) {
            new InstantiationScanner().scan(trees.getPath(element), null);
        }

        return false;
    }

    private static <T> T unwrap(Class<? extends T> iface, T wrapper) {
        T unwrapped = null;
        try {
            Class<?> apiWrappers = wrapper.getClass().getClassLoader()
                    .loadClass("org.jetbrains.jps.javac.APIWrappers");
            Method unwrapMethod = apiWrappers.getDeclaredMethod("unwrap", Class.class, Object.class);
            unwrapped = iface.cast(unwrapMethod.invoke(null, iface, wrapper));
        } catch (Throwable ignored) {
        }
        return unwrapped != null ? unwrapped : wrapper;
    }

    private class InstantiationScanner extends TreePathScanner<Void, Void> {

        @Override
        public Void visitNewClass(NewClassTree node, Void unused) {
            Element constructed = trees.getElement(getCurrentPath());

            if (constructed != null) {
                TypeElement typeElement = (TypeElement) constructed.getEnclosingElement();
                String qualifiedName = typeElement.getQualifiedName().toString();

                if (handlerClassNames.contains(qualifiedName)) {
                    trees.printMessage(
                            Diagnostic.Kind.ERROR,
                            String.format(
                                    "PoshtaR: Direct instantiation of handler '%s' is forbidden.%n" +
                                            "Use the Mediator to dispatch requests.",
                                    qualifiedName
                            ),
                            getCurrentPath().getLeaf(),
                            getCurrentPath().getCompilationUnit()
                    );
                }
            }

            return super.visitNewClass(node, unused);
        }

        @Override
        public Void visitVariable(VariableTree node, Void unused) {
            Element varElement = trees.getElement(getCurrentPath());
            if (varElement == null) return super.visitVariable(node, unused);

            Element enclosingClass = varElement.getEnclosingElement();
            while (enclosingClass != null && enclosingClass.getKind() != ElementKind.CLASS) {
                enclosingClass = enclosingClass.getEnclosingElement();
            }

            // Resolve the variable's type
            String typeName = processingEnv.getTypeUtils()
                    .erasure(varElement.asType())
                    .toString();

            // Only the Mediator itself is allowed to hold handler references
            if (enclosingClass != null) {
                String enclosingName = ((TypeElement) enclosingClass)
                        .getQualifiedName().toString();

                if (enclosingName.equals(MEDIATOR_CLASS)) {
                    return super.visitVariable(node, unused);
                }

                // Handler classes may declare non-handler fields freely,
                // but injecting another handler into a handler is still forbidden
                if (handlerClassNames.contains(enclosingName)
                        && !handlerClassNames.contains(typeName)) {
                    return super.visitVariable(node, unused);
                }
            }

            if (handlerClassNames.contains(typeName)) {
                trees.printMessage(
                        Diagnostic.Kind.ERROR,
                        String.format(
                                "PoshtaR: Direct reference to handler '%s' is forbidden.%n" +
                                        "Handlers must not be injected or declared directly — use the Mediator.",
                                typeName
                        ),
                        getCurrentPath().getLeaf(),
                        getCurrentPath().getCompilationUnit()
                );
            }

            return super.visitVariable(node, unused);
        }
    }
}
