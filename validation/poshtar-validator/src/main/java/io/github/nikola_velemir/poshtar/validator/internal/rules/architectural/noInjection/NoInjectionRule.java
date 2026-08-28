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

package io.github.nikola_velemir.poshtar.validator.internal.rules.architectural.noInjection;

import io.github.nikola_velemir.poshtar.core.mediator.Poshtar;
import io.github.nikola_velemir.poshtar.validator.internal.context.ProcessorContext;
import io.github.nikola_velemir.poshtar.validator.internal.rules.Rule;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.*;
import javax.lang.model.type.TypeMirror;
import java.util.Set;

/**
 * Template rule for no-injection logic.
 * <p>
 * Developer should not be allowed to inject, instantiate, or provide components to their classes at will.
 * Rule prevents a developer from bypassing mediator and pipeline logic entirely.
 * </p>
 *
 * @author Nikola Velemir
 * @version ${project.version}
 * @see io.github.nikola_velemir.poshtar.core.exceptions.AmbiguousHandlerException
 * @since 1.0.0
 */
abstract class NoInjectionRule implements Rule {
    private static final String MEDIATOR_FQN = Poshtar.class.getName();

    /**
     * Checks if the class used is forbidden to be injected. Logs the error if forbidden class is found.
     *
     * @param roundEnv The environment providing access to elements in the current processing round.
     * @param ctx      Instance of context containing all related request, handler and behavior FQNs.
     */
    @Override
    public void validate(RoundEnvironment roundEnv, ProcessorContext ctx) {
        Set<String> forbidden = ctx.getAll();
        if (forbidden.isEmpty()) return;

        for (Element root : roundEnv.getRootElements()) {
            if (root.getKind() != ElementKind.CLASS) continue;
            TypeElement clazz = (TypeElement) root;

            if (clazz.getQualifiedName().contentEquals(MEDIATOR_FQN)) continue;

            if (InjectionBypassChecker.isBypassed(clazz, ctx)) continue;

            checkClassBody((TypeElement) root, forbidden, ctx);
        }
    }

    /**
     * Checks the body of a class in search for forbidden components that should not be injected or instantiated.
     *
     * @param clazz     Class that is under inspection for forbidden components.
     * @param forbidden Set of forbidden FQNs.
     * @param ctx       Instance of context containing all related request, handler and behavior FQNs.
     */
    protected void checkClassBody(TypeElement clazz, Set<String> forbidden, ProcessorContext ctx) {
        for (Element enclosed : clazz.getEnclosedElements()) {

            validateFieldInjection(forbidden, ctx, enclosed);

            validateConstructorInjection(forbidden, ctx, enclosed);
            validateMethodInjection(forbidden, ctx, enclosed);
        }
    }

    /**
     * Checks if forbidden classes are provided thru methods.
     *
     * @param forbidden Set of forbidden FQNs.
     * @param ctx       Instance of context containing all related request, handler and behavior FQNs.
     * @param enclosed  Class that is under inspection for forbidden components.
     */
    protected void validateMethodInjection(Set<String> forbidden, ProcessorContext ctx, Element enclosed) {
        if (enclosed.getKind() == ElementKind.METHOD) {
            ExecutableElement method = (ExecutableElement) enclosed;
            for (VariableElement param : method.getParameters()) {
                if (isForbiddenType(param.asType(), forbidden, ctx)) {
                    logError(param, ctx);
                }
            }
        }
    }

    /**
     * Checks if forbidden classes are provided thru a constructor.
     *
     * @param forbidden Set of forbidden FQNs.
     * @param ctx       Instance of context containing all related request, handler and behavior FQNs.
     * @param enclosed  Class that is under inspection for forbidden components.
     */
    protected void validateConstructorInjection(Set<String> forbidden, ProcessorContext ctx, Element enclosed) {
        if (enclosed.getKind() == ElementKind.CONSTRUCTOR) {
            ExecutableElement constructor = (ExecutableElement) enclosed;
            for (VariableElement param : constructor.getParameters()) {
                if (isForbiddenType(param.asType(), forbidden, ctx)) {
                    logError(param, ctx);
                }
            }
        }
    }

    /**
     * Error logging logic.
     *
     * @param target Element that is bound to the error.
     * @param ctx    Instance of context containing all related request, handler and behavior FQNs.
     */
    protected abstract void logError(Element target, ProcessorContext ctx);

    /**
     * Checks if forbidden classes are provided thru fields.
     *
     * @param forbidden Set of forbidden FQNs.
     * @param ctx       Instance of context containing all related request, handler and behavior FQNs.
     * @param enclosed  Class that is under inspection for forbidden components.
     */
    protected void validateFieldInjection(Set<String> forbidden, ProcessorContext ctx, Element enclosed) {
        if (enclosed.getKind() == ElementKind.FIELD) {
            VariableElement field = (VariableElement) enclosed;
            if (isForbiddenType(field.asType(), forbidden, ctx)) {
                logError(field, ctx);
            }
        }
    }

    /**
     * Method checks if provided type is forbidden.
     * <p>Overrides of this method are to check if specific type provided is the forbidden list.</p>
     *
     * @param type      Type of the component that is being inspected.
     * @param forbidden Set of forbidden FQNs.
     * @param ctx       Instance of context containing all related request, handler and behavior FQNs.
     * @return boolean value if provided type is forbidden.
     */
    protected abstract boolean isForbiddenType(TypeMirror type, Set<String> forbidden, ProcessorContext ctx);
}
