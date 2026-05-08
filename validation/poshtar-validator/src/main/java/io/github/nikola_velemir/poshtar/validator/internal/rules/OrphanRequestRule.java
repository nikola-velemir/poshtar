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

package io.github.nikola_velemir.poshtar.validator.internal.rules;

import io.github.nikola_velemir.poshtar.validator.api.annotations.request.SuppressOrphan;
import io.github.nikola_velemir.poshtar.validator.internal.logger.Logger;
import io.github.nikola_velemir.poshtar.validator.internal.logger.LoggerProvider;
import io.github.nikola_velemir.poshtar.validator.internal.context.ProcessorContext;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.util.Set;

/**
 * Rule that prevents the developer from leaving the request type without a designated handler.
 *
 * <p>
 * Rule disallows the definition of a request class that has no registered handler to handle such request.
 * Rule will prevent {@link io.github.nikola_velemir.poshtar.core.exceptions.HandlerNotFoundException}
 * during runtime by preventing compilation if violated.
 * Developer may use {@link SuppressOrphan} to bypass this rule.
 * </p>
 *
 * @author Nikola Velemir
 * @version ${project.version}
 * @since 1.0.0
 */
class OrphanRequestRule implements Rule {
    private static final Logger logger = LoggerProvider.provideErrorLogger();
    private static final String VIOLATION_MESSAGE = "PoshtaR VIOLATION: No handler registered for request '%s'\n" + "You may use %s to bypass this rule!";

    /**
     *
     * @param roundEnv The environment providing access to elements in the current processing round.
     * @param ctx      Instance of context containing all related request, handler and behavior FQNs.
     */
    @Override
    public void validate(RoundEnvironment roundEnv, ProcessorContext ctx) {
        Set<String> handledTypes = ctx.getHandledRequestTypes();

        for (var requestFqn : ctx.getKnownRequests()) {
            if (handledTypes.contains(requestFqn)) continue;
            TypeElement element = ctx.env.getElementUtils().getTypeElement(requestFqn);
            if (element == null) continue;
            if (hasSuppression(element)) continue;

            logError(ctx, requestFqn);

        }
    }

    private boolean hasSuppression(TypeElement element) {
        return element.getAnnotation(SuppressOrphan.class) != null;

    }

    private static void logError(ProcessorContext ctx, String requestFqn) {
        String errorMessage = String.format(VIOLATION_MESSAGE, requestFqn, SuppressOrphan.class.getCanonicalName());
        Element target = ctx.env.getElementUtils().getTypeElement(requestFqn);
        logger.log(ctx.env, errorMessage, target);
    }
}
