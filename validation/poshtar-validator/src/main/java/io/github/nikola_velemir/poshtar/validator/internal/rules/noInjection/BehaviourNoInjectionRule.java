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

package io.github.nikola_velemir.poshtar.validator.internal.rules.noInjection;

import io.github.nikola_velemir.poshtar.core.pipeline.behaviour.PipelineBehaviour;
import io.github.nikola_velemir.poshtar.validator.internal.logger.Logger;
import io.github.nikola_velemir.poshtar.validator.internal.logger.LoggerProvider;
import io.github.nikola_velemir.poshtar.validator.internal.context.ProcessorContext;

import javax.lang.model.element.*;
import javax.lang.model.type.TypeMirror;
import java.util.Set;

/**
 * Rule that validates behavior no-injection logic.
 * <p>
 * Developer should not be allowed to inject, instantiate, or provide behaviors to their classes at will.
 * Rule prevents a developer from bypassing mediator and pipeline logic entirely.
 * </p>
 *
 * @author Nikola Velemir
 * @version ${project.version}
 * @see io.github.nikola_velemir.poshtar.core.exceptions.AmbiguousHandlerException
 * @since 1.0.0
 */
class BehaviourNoInjectionRule extends NoInjectionRule {

    private static final String BEHAVIOUR_INTERFACE_FQN = PipelineBehaviour.class.getName();
    private static final String VIOLATION_MESSAGE = "PoshtaR VIOLATION: Behaviours cannot be injected, set thru methods or constructor, or manually managed. " + "Use 'Poshtar.send(request)' to interact with this logic.";
    private static final Logger logger = LoggerProvider.provideErrorLogger();

    /**
     * Validation logic to check if provided type is a behavior.
     *
     * @param type      Type of the component that is being inspected.
     * @param forbidden Set of forbidden FQNs.
     * @param ctx       Instance of context containing all related request, handler and behavior FQNs.
     * @return boolean value if provided type is forbidden.
     */
    @Override
    protected boolean isForbiddenType(TypeMirror type, Set<String> forbidden, ProcessorContext ctx) {
        var typeUtils = ctx.env.getTypeUtils();
        var elementUtils = ctx.env.getElementUtils();

        TypeMirror behaviour = elementUtils.getTypeElement(BEHAVIOUR_INTERFACE_FQN).asType();

        TypeMirror erasedType = typeUtils.erasure(type);
        TypeMirror erasedBehaviour = typeUtils.erasure(behaviour);

        return typeUtils.isAssignable(erasedType, erasedBehaviour);
    }


    /**
     * Error logging logic.
     *
     * @param target Element that is bound to the error.
     * @param ctx    Instance of context containing all related request, handler and behavior FQNs.
     */
    @Override
    protected void logError(Element target, ProcessorContext ctx) {
        logger.log(ctx.env, VIOLATION_MESSAGE, target);
    }
}