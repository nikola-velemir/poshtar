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

package io.github.nikola_velemir.poshtar.validator.base.internal.rules;

/**
 * Class acts as an injector to provide {@link RuleValidator} implementation, since implementation is package private.
 */
public interface RuleValidatorProvider {
    /**
     * Provides the implementation of {@link RuleValidator}, as the concrete implementation is package-private.
     *
     * @return Implementation of the {@link RuleValidator}, which will be used by the processor.
     */
     RuleValidator provideValidator();
}
