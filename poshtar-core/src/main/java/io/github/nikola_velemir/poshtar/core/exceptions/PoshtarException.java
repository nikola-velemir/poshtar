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
/**
 * Base exception class for all errors occurring within the Poshtar library.
 * <p>
 * This is a {@link RuntimeException}, meaning it does not require explicit
 * catch blocks, but it serves as the root for more specific exceptions
 * (e.g., registration or dispatching errors). All messages are automatically
 * prefixed with {@code [PoshtaR]} for easier identification in application logs.
 * </p>
 *
 * @author Nikola Velemir
 * @version ${project.version}
 * @since 1.0.0
 */
public class PoshtarException extends RuntimeException {

    /**
     * Creates an exception instance, with the leading tag of the library name.
     * @param message Custom excpetion message.
     */
    public PoshtarException(String message) {
        super("[PoshtaR] " + message);
    }
    /**
     * Creates an exception instance, with the leading tag of the library name.
     * @param message Custom exception message.
     * @param cause Throwable that represents a cause of the error.
     */
    public PoshtarException(String message, Throwable cause) {
        super("[PoshtaR] " + message, cause);
    }
}