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