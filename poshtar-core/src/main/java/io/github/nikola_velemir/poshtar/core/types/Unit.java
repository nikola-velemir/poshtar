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

package io.github.nikola_velemir.poshtar.core.types;

import io.github.nikola_velemir.poshtar.core.request.Request;

/**
 * Represents a type that is used to indicate that a {@link Request}
 * does not return a meaningful value.
 *
 * <p>
 * This class is equivalent to {@code void} or {@link Void}.
 * </p>
 *
 * @author Nikola Velemir
 * @version ${project.version}
 * @since 1.0.0
 */
public final class Unit {
    /**
     * The single shared instance of the {@code Unit} type.
     */
    public static final Unit Value = new Unit();

    private Unit() {
    }
}
