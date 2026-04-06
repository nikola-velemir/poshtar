package org.nikola.velemir.poshtar.opt.rules.deadPipeline;

import com.sun.source.tree.*;
import org.nikola.velemir.poshtar.core.pipeline.delegate.RequestDelegate;
import org.nikola.velemir.poshtar.opt.api.annotations.pipeline.SuppressDead;
import org.nikola.velemir.poshtar.opt.processor.utils.ErrorLogger;
import org.nikola.velemir.poshtar.opt.rules.Rule;
import org.nikola.velemir.poshtar.opt.rules.RuleContext;
import org.nikola.velemir.poshtar.opt.rules.deadPipeline.utils.FlowAnalyser;
import org.nikola.velemir.poshtar.opt.rules.deadPipeline.utils.SuppressionChecker;

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
        if (SuppressionChecker.hasSuppression(method)) return;

        String delegateName = extractDelegateName(method);
        MethodTree tree = ctx.trees.getTree(method);
        if (tree == null) return;

        FlowAnalyser analyser = new FlowAnalyser(ctx, method);
        if (analyser.analyse(tree, delegateName)) return;
        logError(method, ctx);
    }

    private static void logError(ExecutableElement method, RuleContext ctx) {
        String errorMessage = "PoshtaR VIOLATION: Behaviour must either call 'next.handle(request)' or throw an exception. " +
                "\n Logic found no exit path, which will break the pipeline. Use " + SUPPRESS_ANNOTATION_NAME + " if your logic is correct, but bypasses the chain";
        ErrorLogger.logError(ctx.env, errorMessage, method);
    }

    private static String extractDelegateName(ExecutableElement method) {
        return method.getParameters().stream()
                .filter(p -> p.asType().toString().contains(DELEGATE_SIMPLE_NAME))
                .map(p -> p.getSimpleName().toString())
                .findFirst().orElse("next");
    }
}