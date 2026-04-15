package org.nikola.velemir.poshtar.opt.internal.rules;

import com.sun.source.tree.*;
import org.nikola.velemir.poshtar.core.pipeline.delegate.RequestDelegate;
import org.nikola.velemir.poshtar.opt.api.annotations.pipeline.SuppressDead;
import org.nikola.velemir.poshtar.opt.internal.logger.Logger;
import org.nikola.velemir.poshtar.opt.internal.logger.LoggerProvider;
import org.nikola.velemir.poshtar.opt.internal.rules.deadPipeline.FlowAnalyser;
import org.nikola.velemir.poshtar.opt.internal.rules.deadPipeline.SuppressionChecker;
import org.nikola.velemir.poshtar.opt.internal.context.ProcessorContext;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import java.util.Set;


class DeadPipelineRule implements Rule {
    private static final String SUPPRESS_ANNOTATION_NAME = SuppressDead.class.getName();
    private static final String DELEGATE_SIMPLE_NAME = RequestDelegate.class.getSimpleName();
    private static final String VIOLATION_MESSAGE = "PoshtaR VIOLATION: Behaviour must either call 'next.handle(request)' or throw an exception.\n Logic found no exit path, which will break the pipeline. Use %s if your logic is correct, but bypasses the chain";
    private static final Logger logger = LoggerProvider.provideWarningLogger();

    @Override
    public void validate(RoundEnvironment roundEnv, ProcessorContext ctx) {
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

    private void validateMethodFlow(ExecutableElement method, ProcessorContext ctx) {
        if (SuppressionChecker.hasSuppression(method)) return;

        String delegateName = extractDelegateName(method);
        MethodTree tree = ctx.trees.getTree(method);
        if (tree == null) return;

        FlowAnalyser analyser = new FlowAnalyser(ctx, method);
        if (analyser.analyse(tree, delegateName)) return;
        logError(method, ctx);
    }

    private static void logError(ExecutableElement method, ProcessorContext ctx) {
        String errorMessage = String.format(VIOLATION_MESSAGE, SUPPRESS_ANNOTATION_NAME);
        logger.log(ctx.env, errorMessage, method);
    }

    private static String extractDelegateName(ExecutableElement method) {
        return method.getParameters().stream()
                .filter(p -> p.asType().toString().contains(DELEGATE_SIMPLE_NAME))
                .map(p -> p.getSimpleName().toString())
                .findFirst().orElse("next");
    }
}