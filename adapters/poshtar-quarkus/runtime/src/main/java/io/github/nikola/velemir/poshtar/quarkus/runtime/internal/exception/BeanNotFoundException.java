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

package io.github.nikola.velemir.poshtar.quarkus.runtime.internal.exception;

import io.github.nikola_velemir.poshtar.core.exceptions.PoshtarException;

/**
 * Thrown during runtime initialization if a required PoshtaR component
 * cannot be resolved within the active Quarkus CDI container.
 *
 * <p>
 * This exception indicates a synchronization gap between build-time component discovery
 * and runtime context state, typically caused by a class missing an appropriate scope
 * or being prematurely stripped by container optimization.
 * </p>
 *
 * @author Nikola Velemir
 * @since 1.0.0
 */
public class BeanNotFoundException extends PoshtarException {
    /**
     * Constructs a new exception instance with a customized detail message.
     *
     * @param message the detail message explaining the cause of the failure
     */
    public BeanNotFoundException(String message) {
        super(message);
    }

    /**
     * Constructs a new exception instance pre-formatted with the naming information
     * of the missing component class definition.
     *
     * @param beanClass the class metadata type of the missing CDI bean target
     */
    public BeanNotFoundException(Class<?> beanClass) {
        super("No CDI bean for: " + beanClass.getName());
    }
}
