package org.nikola.velemir.poshtar.opt.rules.deadPipeline;

import com.sun.source.tree.MethodInvocationTree;
import com.sun.source.tree.MethodTree;
import com.sun.source.tree.ThrowTree;
import com.sun.source.util.TreeScanner;
import org.nikola.velemir.poshtar.core.annotations.Behaviour;
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
import java.util.stream.Collectors;

public class DeadPipelineRule implements Rule {
    private static final String BEHAVIOUR_ANNOTATION_NAME = Behaviour.class.getName();
    private static final String SUPPRESS_ANNOTATION_NAME = SuppressDead.class.getName();

    @Override
    public void validate(RoundEnvironment roundEnv, RuleContext ctx) {
        TypeElement behaviourAnot = ctx.env.getElementUtils().getTypeElement(BEHAVIOUR_ANNOTATION_NAME);
        if (behaviourAnot == null) return;
        Set<TypeElement> behaviours = extractBehaviourElements(roundEnv, behaviourAnot);
        for (TypeElement behaviour : behaviours) {
            for (Element enclosed : behaviour.getEnclosedElements()) {
                if (enclosed.getKind() == ElementKind.METHOD && enclosed.getSimpleName().contentEquals("handle")) {
                    validateMethodFlow((ExecutableElement) enclosed, ctx);
                }
            }
        }
    }

    private void validateMethodFlow(ExecutableElement method, RuleContext ctx) {
        if (hasSuppression(method)) return;

        MethodTree tree = ctx.trees.getTree(method);
        FlowScanner scanner = new FlowScanner();

        scanner.scan(tree.getBody(), null);
        if (scanner.hasExitPath()) return;

        ctx.env.getMessager().printMessage(
                Diagnostic.Kind.ERROR,
                "PoshtaR VIOLATION: Behaviour must either call 'next.handle(request)' or throw an exception. " +
                        "\n Logic found no exit path, which will break the pipeline. Use " + SUPPRESS_ANNOTATION_NAME + " if your logic is correct, but bypasses the chain",
                method
        );

    }

    private static Set<TypeElement> extractBehaviourElements(RoundEnvironment roundEnv, TypeElement behaviourAnot) {
        return roundEnv.getElementsAnnotatedWith(behaviourAnot)
                .stream()
                .filter(te -> te.getKind() == ElementKind.CLASS)
                .map(te -> (TypeElement) te)
                .collect(Collectors.toSet());
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
        private boolean foundNextCall = false;
        private boolean foundThrow = false;

        @Override
        public Void visitMethodInvocation(MethodInvocationTree node, Void unused) {
            if (node.getMethodSelect().toString().contains(".handle")) {
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
