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