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

package io.github.nikola_velemir.poshtar.validator.internal.unwrapper.stategy;

import java.util.List;
/**
 * Provider that manages the registration and retrieval of IDE unwrapping strategies.
 * <p>
 * This class serves as an injector for the {@link UnwrapStrategy} implementations used by the
 * annotation processor.
 * </p>
 *
 * @author Nikola Velemir
 * @version ${project.version}
 * @since 1.0.0
 */
public class StrategyProvider {
    /**
     * Returns a list of available unwrapping strategies.
     *
     * @return A list of initialized {@link UnwrapStrategy} instances.
     */
    public static List<UnwrapStrategy> provideStrategies(){
        return List.of(
                new JetBrainsUnwrapStrategy(),
                new EclipseUnwrapStrategy(),
                new DefaultUnwrapStrategy()
        );
    }
}
