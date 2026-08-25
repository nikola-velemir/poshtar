/*
 * Copyright (C) 2026 Nikola (nvelem.nikola@gmail.com)
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */

package io.github.nikola_velemir.poshtar.validator.internal.rules.registration;

import io.github.nikola_velemir.poshtar.validator.api.annotations.request.SuppressOrphan;
import io.github.nikola_velemir.poshtar.validator.base.internal.context.ProcessorContext;
import io.github.nikola_velemir.poshtar.validator.base.internal.logger.Logger;
import io.github.nikola_velemir.poshtar.validator.base.internal.logger.LoggerProvider;
import io.github.nikola_velemir.poshtar.validator.base.internal.rules.Rule;


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
    private static final String VIOLATION_MESSAGE = "PoshtaR VIOLATION: No handler registered for request '%s'%n" + "You may use %s to bypass this rule!";

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
