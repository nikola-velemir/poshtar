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

package io.github.nikola_velemir.poshtar.validator.base.internal.logger;

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
