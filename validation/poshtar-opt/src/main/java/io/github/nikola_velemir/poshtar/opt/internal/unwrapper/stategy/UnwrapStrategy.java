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

package io.github.nikola_velemir.poshtar.opt.internal.unwrapper.stategy;

/**
 * Defines a strategy for extracting compiler implementations from IDE proxies.
 * <p>
 * Implementations of this interface handle the vendor-specific logic required to
 * "unwrap" objects like the {@link javax.annotation.processing.ProcessingEnvironment}.
 * This is necessary because IDEs often wrap these objects in proxies that hide
 * internal APIs (such as the {@code com.sun.source.util.Trees} instance).
 * </p>
 *
 * @author Nikola Velemir
 * @version ${project.version}
 * @see io.github.nikola_velemir.poshtar.opt.internal.unwrapper.IdeUnwrapper
 * @since 1.0.0
 */
public interface UnwrapStrategy {
    /**
     * Determines if this strategy is capable of unwrapping the provided object.
     *
     * @param wrapper The object to check for compatibility.
     * @return {@code true} if this strategy can unwrap the object; {@code false} otherwise.
     */
    boolean supports(Object wrapper);

    /**
     * Unwraps the provided object to reveal the requested interface or implementation.
     *
     * @param <T>     The target type to unwrap to.
     * @param iface   The class literal of the target type.
     * @param wrapper The proxied object to be unwrapped.
     * @return The raw, underlying implementation cast to the requested type.
     * @throws ClassCastException if the unwrapped object cannot be cast to {@code T}.
     */
    <T> T unwrap(Class<? extends T> iface, T wrapper);

}
