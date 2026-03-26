package org.nikola.velemir.poshtar.opt.rules.deadPipeline;

import com.sun.source.tree.MethodInvocationTree;
import com.sun.source.tree.MethodTree;
import com.sun.source.tree.ThrowTree;
import com.sun.source.util.TreeScanner;
import org.nikola.velemir.poshtar.core.pipeline.delegate.RequestDelegate;
import org.nikola.velemir.poshtar.opt.annotations.suppression.SuppressDead;
import org.nikola.velemir.poshtar.opt.rules.Rule;
import org.nikola.velemir.poshtar.opt.rules.RuleContext;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.util.Set;

public class DeadPipelineRule implements Rule {
    private static final String SUPPRESS_ANNOTATION_NAME = SuppressDead.class.getName();
    public static final String DELEGATE_SIMPLE_NAME = RequestDelegate.class.getSimpleName();

    @Override
    public void validate(RoundEnvironment roundEnv, RuleContext ctx) {
        Set<String> behaviourFqns = ctx.getKnownBehaviours();

        for (String fqn : behaviourFqns) {
            TypeElement behaviour = ctx.env.getElementUtils().getTypeElement(fqn);

            if (behaviour == null) continue;

            for (Element enclosed : behaviour.getEnclosedElements()) {
                if (enclosed.getKind() == ElementKind.METHOD &&
                        enclosed.getSimpleName().contentEquals("handle")) {

                    validateMethodFlow((ExecutableElement) enclosed, ctx);
                }
            }
        }
    }

    private void validateMethodFlow(ExecutableElement method, RuleContext ctx) {
        if (hasSuppression(method)) return;

        String delegateName = extractDelegateName(method);
        MethodTree tree = ctx.trees.getTree(method);
        if (tree == null) return;

        FlowScanner scanner = new FlowScanner(delegateName);

        scanner.scan(tree.getBody(), null);
        if (scanner.hasExitPath()) return;

        logError(method, ctx);

    }

    private static void logError(ExecutableElement method, RuleContext ctx) {
        ctx.env.getMessager().printMessage(
                Diagnostic.Kind.ERROR,
                "PoshtaR VIOLATION: Behaviour must either call 'next.handle(request)' or throw an exception. " +
                        "\n Logic found no exit path, which will break the pipeline. Use " + SUPPRESS_ANNOTATION_NAME + " if your logic is correct, but bypasses the chain",
                method
        );
    }

    private static String extractDelegateName(ExecutableElement method) {
        return method.getParameters().stream()
                .filter(p -> p.asType().toString().contains(DELEGATE_SIMPLE_NAME))
                .map(p -> p.getSimpleName().toString())
                .findFirst().orElse("next");
    }

    private boolean hasSuppression(ExecutableElement method) {
        boolean methodSuppressed = method.getAnnotationMirrors().stream()
                .anyMatch(mirror -> mirror.getAnnotationType().asElement()
                        .toString().equals(SUPPRESS_ANNOTATION_NAME));
        if (methodSuppressed) return true;

        Element enclosing = method.getEnclosingElement();
        return enclosing.getAnnotationMirrors().stream()
                .anyMatch(mirror -> mirror.getAnnotationType().asElement()
                        .toString().equals(SUPPRESS_ANNOTATION_NAME));
    }

    private static class FlowScanner extends TreeScanner<Void, Void> {
        private final String delegateName;
        private boolean foundNextCall = false;
        private boolean foundThrow = false;

        private FlowScanner(String name) {
            delegateName = name;
        }

        @Override
        public Void visitMethodInvocation(MethodInvocationTree node, Void unused) {
            String call = node.getMethodSelect().toString();

            if (call.equals(delegateName + ".handle") || call.endsWith("." + delegateName + ".handle")) {
                foundNextCall = true;
            }
            return super.visitMethodInvocation(node, unused);
        }

        @Override
        public Void visitThrow(ThrowTree node, Void unused) {
            foundThrow = true;
            return super.visitThrow(node, unused);
        }

        public boolean hasExitPath() {
            return foundNextCall || foundThrow;
        }
    }
}
