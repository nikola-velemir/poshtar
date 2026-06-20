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

package io.github.nikola_velemir.poshtar.validator.internal.rules.finality;

import io.github.nikola_velemir.poshtar.validator.internal.context.ProcessorContext;
import io.github.nikola_velemir.poshtar.validator.internal.logger.Logger;
import io.github.nikola_velemir.poshtar.validator.internal.logger.LoggerProvider;
import io.github.nikola_velemir.poshtar.validator.internal.rules.Rule;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import java.util.Set;

public abstract class FinalityRule implements Rule {

    private static final Logger logger = LoggerProvider.provideErrorLogger();


    protected abstract String getViolationMessage(String requestFqn);
    protected abstract Set<String> getFQNs(ProcessorContext ctx);
    /**
     * Validates the finality of request classes. Logs the error if finality is violated.
     * @param roundEnv The environment providing access to elements in the current processing round.
     * @param ctx      Instance of context containing all related request, handler and behavior FQNs.
     */
    @Override
    public void validate(RoundEnvironment roundEnv, ProcessorContext ctx) {
        var fqns = getFQNs(ctx);
        for (var notificationFqn : fqns) {

            TypeElement element = ctx.env.getElementUtils().getTypeElement(notificationFqn);

            boolean isFinalOrRecord = checkIfFinalOrRecord(element);
            if (!isFinalOrRecord) logError(ctx, notificationFqn, element);

        }
    }

    private boolean checkIfFinalOrRecord(TypeElement element) {
        boolean isRecord = element.getKind() == ElementKind.RECORD;
        boolean isFinal = element.getModifiers().contains(Modifier.FINAL);
        return isRecord || isFinal;

    }

    private void logError(ProcessorContext ctx, String requestFqn, TypeElement targetClass) {
        String finalityViolationMessage = getViolationMessage(requestFqn);
        String errorMessage = finalityViolationMessage;
        logger.log(ctx.env, errorMessage, targetClass);

    }
}