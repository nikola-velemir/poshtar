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

package io.github.nikola_velemir.poshtar.validator.base.internal.unwrapper;


import io.github.nikola_velemir.poshtar.validator.base.internal.unwrapper.stategy.StrategyProvider;

/**
 * Utility class for unwrapping compiler implementation classes to ensure compatibility
 * across different Integrated Development Environments (IDEs).
 * <p>
 * Various IDEs (such as IntelliJ IDEA or Eclipse) wrap the standard
 * {@link javax.annotation.processing.ProcessingEnvironment} and its components
 * in proxy objects. This wrapping can prevent the annotation processor from
 * accessing vendor-specific or internal APIs required for advanced diagnostics
 * and source-tree inspection.
 * </p>
 *
 * @author Nikola Velemir
 * @version ${project.version}
 * @since 1.0.0
 */
public class IdeUnwrapper {
    /**
     * Attempts to unwrap a given compiler component to its underlying implementation.
     * <p>
     * Iterates through available {@code UnwrapStrategy} implementations provided by
     * the {@code StrategyProvider}. If a strategy supports the given wrapper, it
     * is used to extract the raw instance; otherwise, the original wrapper is
     * returned as a fallback.
     * </p>
     *
     * @param <T>     The expected type of the unwrapped component.
     * @param iface   The class or interface type to cast the unwrapped result to.
     * @param wrapper The potentially wrapped component (e.g., ProcessingEnvironment).
     * @return The unwrapped instance if a matching strategy is found,
     *         otherwise the original {@param wrapper}.
     */
    public static <T> T unwrap(Class<? extends T> iface, T wrapper) {
        var strategies = StrategyProvider.provideStrategies();
        return strategies.stream()
                .filter(s -> s.supports(wrapper))
                .findFirst()
                .map(s -> s.unwrap(iface, wrapper))
                .orElse(wrapper);
    }
}
