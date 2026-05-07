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
