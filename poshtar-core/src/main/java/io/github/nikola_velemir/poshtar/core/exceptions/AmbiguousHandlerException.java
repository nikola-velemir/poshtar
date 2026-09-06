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

import java.util.List;
/**
 * Exception thrown when multiple handlers are discovered or registered for a single {@link io.github.nikola_velemir.poshtar.core.request.Request} type.
 * <p>
 * The Poshtar requires a strict one-to-one mapping for requests to ensure
 * deterministic behavior.
 * </p>
 *
 * @author Nikola Velemir
 * @version ${project.version}
 * @since 1.0.0
 */
public class AmbiguousHandlerException extends PoshtarException {
    /**
     * Constructs a new exception detailing the conflicting handlers for a specific request type.
     *
     * @param requestClass The class of the request that has multiple handlers assigned.
     * @param handlers     A list of the handler classes that were found to be in conflict.
     */
    public AmbiguousHandlerException(Class<?> requestClass, List<Class<?>> handlers) {
        super(
                String.format("There are multiple handlers registered for type [%s]: %s",
                        requestClass.getSimpleName(), handlers.toString())
        );
    }
}
