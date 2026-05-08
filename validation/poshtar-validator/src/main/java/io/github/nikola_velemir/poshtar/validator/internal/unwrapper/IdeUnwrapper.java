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

package io.github.nikola_velemir.poshtar.validator.internal.unwrapper;

import io.github.nikola_velemir.poshtar.validator.internal.unwrapper.stategy.*;
import io.github.nikola_velemir.poshtar.validator.internal.unwrapper.stategy.StrategyProvider;

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
