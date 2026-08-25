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
import javax.tools.Diagnostic;

/**
 * Base class for logger implementations. Class provides a default method for message logging.
 *
 * <p>
 * Classes extending this class provide their message kind,
 * or in other words, severity of the message.
 * </p>
 *
 * @author Nikola Velemir
 * @version ${project.version}
 * @since 1.0.0
 */
abstract class CoreLogger implements Logger {
    /**
     * Returns the diagnostic severity level for this logger implementation.
     *
     * @return The {@link Diagnostic.Kind} (e.g., ERROR, WARNING) to be used.
     */
    protected abstract Diagnostic.Kind getKind();

    /**
     * Reports a diagnostic message to the compiler's messager with a standard PoshtaR prefix.
     * <p>
     * This method aggregates the provided error message dispatches
     * it using the {@link Diagnostic.Kind} provided by the implementation's {@code getKind()} method.
     * It enables the IDE to provide "click-to-jump" functionality
     * directly to the source of the architectural violation.
     * </p>
     *
     * @param env          The current processing environment.
     * @param errorMessage The descriptive text explaining the violation or error.
     * @param element      The source code element (class, method, or field) to which this message applies.
     */
    public void log(ProcessingEnvironment env, String errorMessage, Element element) {
        String aggregatedMessage = "[PoshtaR] " + errorMessage;
        env.getMessager().printMessage(
                getKind(),
                aggregatedMessage,
                element);
    }
}
