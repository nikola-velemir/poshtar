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

package io.github.nikola_velemir.poshtar.validator.internal.rules;

import io.github.nikola_velemir.poshtar.validator.internal.context.ProcessorContext;

import javax.annotation.processing.RoundEnvironment;

/**
 * Interface defines a rule that models a validation logic.
 *
 * <p>
 * Implementations of this interface are responsible for inspecting the source code
 * during compilation to ensure that Poshtar components (Handlers, Requests, and Behaviors)
 * adhere to the library's required structure and design patterns.
 * </p>
 *
 * @author Nikola Velemir
 * @version ${project.version}
 * @since 1.0.0
 */
public interface Rule {
    /**
     * Executes the validation logic for this specific rule.
     * If a violation is found, logic should dispatch an error or a warning message to a compiler, depending on its severity.
     *
     * @param roundEnv The environment providing access to elements in the current processing round.
     * @param ctx      Instance of context containing all related request, handler and behavior FQNs.
     */
    void validate(RoundEnvironment roundEnv, ProcessorContext ctx);

}