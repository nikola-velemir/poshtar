//package org.nikola.velemir.poshtar.opt.processor;
//
//import com.google.auto.service.AutoService;
//import com.sun.source.tree.NewClassTree;
//import com.sun.source.util.TreePathScanner;
//import com.sun.source.util.Trees;
//
//import javax.annotation.processing.*;
//import javax.lang.model.SourceVersion;
//import javax.lang.model.element.Element;
//import javax.lang.model.element.ElementKind;
//import javax.lang.model.element.TypeElement;
//import javax.tools.Diagnostic;
//import java.util.HashMap;
//import java.util.HashSet;
//import java.util.Set;
//
//@AutoService(Processor.class)
//@SupportedAnnotationTypes("*")
//@SupportedSourceVersion(SourceVersion.RELEASE_21)
//public class InstantiationProcessor extends AbstractProcessor {
//    private Trees trees;
//    private final Set<String> handlerClassNames = new HashSet<>();
//
//    @Override
//    public synchronized void init(ProcessingEnvironment processingEnv) {
//        super.init(processingEnv);
//        trees = Trees.instance(processingEnv);
//    }
//
//    @Override
//    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
//        if (roundEnv.processingOver()) return false;
//
//        // 1. SAFE LOOKUP: Prevent IllegalArgumentException if the annotation isn't in the environment
//        TypeElement handlerAnnotation = processingEnv.getElementUtils()
//                .getTypeElement("org.nikola.velemir.poshtar.core.annotations.Handler");
//
//        if (handlerAnnotation != null) {
//            for (Element element : roundEnv.getElementsAnnotatedWith(handlerAnnotation)) {
//                if (element.getKind() == ElementKind.CLASS) {
//                    handlerClassNames.add(((TypeElement) element).getQualifiedName().toString());
//                }
//            }
//        }
//
//        // 2. SAFE SCAN: Prevent IllegalArgumentException by checking for null paths
//        for (Element element : roundEnv.getRootElements()) {
//            com.sun.source.util.TreePath path = trees.getPath(element);
//
//            // If path is null, it means this element doesn't have a source AST
//            // (e.g., binary class, package element). Skip it.
//            if (path != null) {
//                new InstantiationScanner().scan(path, null);
//            }
//        }
//
//        return false;
//    }
//    private class InstantiationScanner extends TreePathScanner<Void, Void> {
//
//        @Override
//        public Void visitNewClass(NewClassTree node, Void unused) {
//            // Resolve the constructor call to its TypeElement
//            Element constructedType = trees.getElement(getCurrentPath());
//
//            if (constructedType != null) {
//                TypeElement typeElement = (TypeElement) constructedType
//                        .getEnclosingElement(); // constructor → its class
//
//                String qualifiedName = typeElement.getQualifiedName().toString();
//
//                if (handlerClassNames.contains(qualifiedName)) {
//                    trees.printMessage(
//                            Diagnostic.Kind.ERROR,
//                            String.format(
//                                    "PoshtaR: Direct instantiation of handler '%s' is forbidden.%n" +
//                                            "Handlers must be registered and invoked through the PoshtaR pipeline.",
//                                    qualifiedName
//                            ),
//                            getCurrentPath().getLeaf(),
//                            getCurrentPath().getCompilationUnit()
//                    );
//                }
//            }
//
//            return super.visitNewClass(node, unused); // keep walking nested expressions
//        }
//    }
//}
