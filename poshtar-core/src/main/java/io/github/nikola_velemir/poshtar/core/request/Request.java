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

package io.github.nikola_velemir.poshtar.core.request;

/**
 * Defines a request that can be dispatched to the mediator.
 * <p>
 * Class implementing this interface represents a command or a query, to be handled by its specific handler.
 * {@link io.github.nikola_velemir.poshtar.core.mediator.Poshtar}'s {@code send} method processes this request, and returns an object of {@code TResponse} type.
 * </p>
 *
 * <p>For requests that do not return a specific value, use
 * {@link io.github.nikola_velemir.poshtar.core.types.Unit} as the response type.
 * </p>
 *
 * @param <TResponse> The type of the response expected after processing this request.
 * @author Nikola Velemir
 * @version ${project.version}
 * @since 1.0.0
 */
public interface Request<TResponse> {
}
