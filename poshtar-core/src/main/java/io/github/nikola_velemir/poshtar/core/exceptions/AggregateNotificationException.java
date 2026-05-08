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

package io.github.nikola_velemir.poshtar.core.exceptions;

import java.io.PrintStream;
import java.util.List;

/**
 * Exception that collects and wraps failures occurring during a
 * notification broadcast.
 * <p>
 * When a {@link io.github.nikola_velemir.poshtar.core.notification.Notification}
 * is published, multiple subscribers may attempt to process it. If one or more
 * subscribers throw an exception, all of them are collected into this
 * aggregate container rather than stopping at the first failure.
 * </p>
 *
 * @author Nikola Velemir
 * @version ${project.version}
 * @since 1.0.0
 */
public class AggregateNotificationException extends PoshtarException {
    /**
     * List of wrapped errors.
     */
    private final List<Throwable> errors;

    /**
     * Instantiates a new instance of this exception, wrapping the list of provided errors.
     *
     * @param errors List of errors to be wrapped.
     */
    public AggregateNotificationException(List<Throwable> errors) {
        super(formatMessage(errors));
        this.errors = List.copyOf(errors);
    }

    /**
     * Provides the list of wrapped errors.
     *
     * @return List of wrapped errors.
     */
    public List<Throwable> getErrors() {
        return errors;
    }

    /**
     * Formats a summary message including the count and brief description
     * of each failure.
     *
     * @param errors The list of errors to format.
     * @return A formatted string summary.
     */
    private static String formatMessage(List<Throwable> errors) {
        StringBuilder sb = new StringBuilder();
        sb.append(errors.size()).append(" failures occurred during notification dispatch:\n");
        for (int i = 0; i < errors.size(); i++) {
            sb.append("  [#").append(i + 1).append("] ")
                    .append(errors.get(i).getClass().getSimpleName()).append(": ")
                    .append(errors.get(i).getMessage()).append("\n");
        }
        return sb.toString();
    }

    /**
     * Prints the stack trace of the aggregate exception, followed by the
     * detailed stack traces of all suppressed sub-exceptions.
     *
     * @param s The {@link PrintStream} to use for output.
     */
    @Override
    public void printStackTrace(PrintStream s) {
        super.printStackTrace(s);
        errors.forEach(e -> {
            s.println("\n--- Sub-exception details ---");
            e.printStackTrace(s);
        });
    }
}
