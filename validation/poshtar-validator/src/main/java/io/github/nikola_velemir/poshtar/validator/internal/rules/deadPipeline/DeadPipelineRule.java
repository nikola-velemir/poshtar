/*
 * Copyright 2026 Nikola Velemir
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.nikola_velemir.poshtar.validator.internal.rules.deadPipeline;

import com.sun.source.tree.*;
import io.github.nikola_velemir.poshtar.core.pipeline.delegate.RequestDelegate;
import io.github.nikola_velemir.poshtar.validator.api.annotations.pipeline.SuppressDead;
import io.github.nikola_velemir.poshtar.validator.internal.logger.Logger;
import io.github.nikola_velemir.poshtar.validator.internal.logger.LoggerProvider;
import io.github.nikola_velemir.poshtar.validator.internal.rules.Rule;
import io.github.nikola_velemir.poshtar.validator.internal.context.ProcessorContext;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import java.util.Set;

/**
 * Validation rule that ensures pipeline continuity by detecting "Dead Pipelines."
 * <p>
 * In a Chain of Responsibility pattern, a {@code Behaviour} must either:
 * <ol>
 *     <li>Forward the request to the next component using {@code next.handle(request)}.</li>
 *     <li>Terminate the flow intentionally by throwing a {@link RuntimeException}.</li>
 * </ol>
 * </p>
 * <p>
 * If a behavior finishes execution without doing either, the request is effectively
 * dropped, leading to silent failures at runtime. This rule uses the Abstract Syntax
 * Tree (AST) to verify that all logical branches in the {@code handle} method provide
 * an exit path that maintains pipeline integrity.
 * </p>
 *
 * @author Nikola Velemir
 * @version ${project.version}
 * @since 1.0.0
 * @see io.github.nikola_velemir.poshtar.validator.api.annotations.pipeline.SuppressDead
 */
class DeadPipelineRule implements Rule {
    private static final String SUPPRESS_ANNOTATION_NAME = SuppressDead.class.getName();
    private static final String DELEGATE_SIMPLE_NAME = RequestDelegate.class.getSimpleName();
    private static final String VIOLATION_MESSAGE = "PoshtaR VIOLATION: Behaviour must either call 'next.handle(request)' or throw an exception.\n Logic found no exit path, which will break the pipeline. Use %s if your logic is correct, but bypasses the chain";
    private static final Logger logger = LoggerProvider.provideWarningLogger();

    /**
     * Iterates through all registered behaviors and validates their 'handle' methods.
     *
     * @param roundEnv The current processing round environment.
     * @param ctx      The shared context containing discovered behaviors and AST utilities.
     */
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