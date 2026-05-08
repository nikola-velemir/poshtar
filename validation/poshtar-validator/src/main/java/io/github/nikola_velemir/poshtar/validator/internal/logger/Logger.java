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

package io.github.nikola_velemir.poshtar.validator.internal.logger;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
/**
 * Defines a contract for reporting diagnostic messages during the annotation processing phase.
 * <p>
 * This interface provides a simplified abstraction over the {@link javax.annotation.processing.Messager},
 * allowing the Poshtar annotation processor to report architectural violations, errors,
 * or warnings directly to the compiler's output or the IDE's error view.
 * </p>
 *
 * @author Nikola Velemir
 * @version ${project.version}
 * @since 1.0.0
 */
public interface Logger {
    /**
     * Dispatches a message to the compiler's diagnostic output.
     * <p>
     * {@link Element} allows the compiler to
     * highlight the specific line of code that caused the message, providing
     * immediate visual feedback to the developer.
     * </p>
     *
     * @param env          The current processing environment.
     * @param errorMessage The descriptive text explaining the violation or error.
     * @param element      The source code element (class, method, or field) to which this message applies.
     */
    void log(ProcessingEnvironment env, String errorMessage, Element element);
}
