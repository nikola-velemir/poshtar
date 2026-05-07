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

package io.github.nikola_velemir.poshtar.opt.internal.logger;

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
     * @return The {@link javax.tools.Diagnostic.Kind} (e.g., ERROR, WARNING) to be used.
     */
    protected abstract Diagnostic.Kind getKind();

    /**
     * Reports a diagnostic message to the compiler's messager with a standard PoshtaR prefix.
     * <p>
     * This method aggregates the provided error message dispatches
     * it using the {@link javax.tools.Diagnostic.Kind} provided by the implementation's {@code getKind()} method.
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
