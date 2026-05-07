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

package io.github.nikola_velemir.poshtar.opt.internal.rules;

import io.github.nikola_velemir.poshtar.opt.api.annotations.request.SuppressOrphan;
import io.github.nikola_velemir.poshtar.opt.internal.logger.Logger;
import io.github.nikola_velemir.poshtar.opt.internal.logger.LoggerProvider;
import io.github.nikola_velemir.poshtar.opt.internal.context.ProcessorContext;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;

/**
 * Rule that prevents the developer from creating request class inheritance.
 *
 * <p>
 * Rule disallows the inheritance between classes that extend
 * {@link io.github.nikola_velemir.poshtar.core.request.Request},
 * by forcing such classes to be records or declared final.
 * </p>
 *
 * @author Nikola Velemir
 * @version ${project.version}
 * @since 1.0.0
 */
class RequestFinalityRule implements Rule {

    private static final String FINALITY_VIOLATED_MESSAGE = "PoshtaR: Finality Violated! Request '%s' must be final or a record!";
    private static final Logger logger = LoggerProvider.provideErrorLogger();

    /**
     * Validates the finality of request classes. Logs the error if finality is violated.
     * @param roundEnv The environment providing access to elements in the current processing round.
     * @param ctx      Instance of context containing all related request, handler and behavior FQNs.
     */
    @Override
    public void validate(RoundEnvironment roundEnv, ProcessorContext ctx) {

        for (var requestFqn : ctx.getKnownRequests()) {
            if ("BEHAVIOUR".equals(requestFqn)) continue;

            TypeElement element = ctx.env.getElementUtils().getTypeElement(requestFqn);

            boolean isFinalOrRecord = checkIfFinalOrRecord(element);
            if (!isFinalOrRecord) logError(ctx, requestFqn, element);

        }
    }

    private static boolean checkIfFinalOrRecord(TypeElement element) {
        boolean isRecord = element.getKind() == ElementKind.RECORD;
        boolean isFinal = element.getModifiers().contains(Modifier.FINAL);
        return isRecord || isFinal;

    }

    private static void logError(ProcessorContext ctx, String requestFqn, TypeElement targetClass) {
        String errorMessage = String.format(FINALITY_VIOLATED_MESSAGE, requestFqn);
        logger.log(ctx.env, errorMessage, targetClass);

    }
}
