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
 * Exception thrown when the mediator attempts to send a request that has no
 * associated handler registered in the {@link io.github.nikola_velemir.poshtar.core.request.registry.RequestRegistry}.
 * <p>
 * Occurs if a handler class is missing the proper annotations,
 * or if the package containing the handler was not included.
 * </p>
 *
 * @author Nikola Velemir
 * @version ${project.version}
 * @since 1.0.0
 */
public class HandlerNotFoundException extends PoshtarException {
    /**
     * Instantiates a new instance of the exception, with the provided class of the orphaned request.
     * @param requestClass Class literal of the orphaned request.
     */
    public HandlerNotFoundException(Class<?> requestClass) {
        super(
                String.format(
                        "No handler found for type: [%s].",
                        requestClass.getSimpleName()
                )
        );
    }
}
