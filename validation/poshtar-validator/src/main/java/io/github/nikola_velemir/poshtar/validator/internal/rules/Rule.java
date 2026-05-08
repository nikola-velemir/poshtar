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