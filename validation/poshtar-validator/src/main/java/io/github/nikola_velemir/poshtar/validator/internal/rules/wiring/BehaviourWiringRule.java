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

package io.github.nikola_velemir.poshtar.validator.internal.rules.wiring;

import io.github.nikola_velemir.poshtar.core.annotations.Behaviour;
import io.github.nikola_velemir.poshtar.core.pipeline.behaviour.PipelineBehaviour;
import io.github.nikola_velemir.poshtar.validator.base.internal.context.ProcessorContext;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;

class BehaviourWiringRule extends WiringRule {

    private TypeMirror behaviourErasure;

    public BehaviourWiringRule() {
        super(Behaviour.class);
    }

    @Override
    protected void validateAnnotationAndImplementation(ProcessorContext ctx, TypeElement typeElement) {
        var types = ctx.getTypes();

        boolean implementsBehaviour = types.isAssignable(types.erasure(typeElement.asType()), behaviourErasure);


        boolean hasAnnotation = this.hasAnnotation(typeElement);

        if (implementsBehaviour && !hasAnnotation) {
            logger.log(ctx.env, "Missing @Behaviour annotation on PipelineBehaviour implementation.", typeElement);
        }

        if (hasAnnotation && !implementsBehaviour) {
            logger.log(ctx.env, "Class annotated with @Behaviour must implement PipelineBehaviour.", typeElement);
        }
    }
    /**
     * Initializes the behavior interface type erasure, used to validate if wiring is correct.
     * @param ctx Instance of context containing all related request, handler and behavior FQNs.
     */
    @Override
    protected void initErasures(ProcessorContext ctx) {
        var types = ctx.getTypes();

        var elements = ctx.getElements();
        TypeElement behaviourIface = elements.getTypeElement(PipelineBehaviour.class.getCanonicalName());

        if (behaviourIface == null) return;

        behaviourErasure = types.erasure(behaviourIface.asType());
    }

}
