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
 * Defines a chain of responsibility, through which request will be passed during handling.
 * <p>
 * Class effectively wraps the {@link io.github.nikola_velemir.poshtar.core.pipeline.behaviour.PipelineBehaviour}
 * and {@link io.github.nikola_velemir.poshtar.core.request.handler.RequestHandler} classes,
 * forming an onion structure.
 * </p>
 *
 * <p>
 * Upon calling {@link io.github.nikola_velemir.poshtar.core.mediator.Poshtar}'s {@code send} method,
 * request will be passed down the chain of behavior classes that support {@link TRequest} generic constraint,
 * reaching handler in the end.
 * </p>
 *
 * @param <TRequest>  Type of request chain handles.
 * @param <TResponse> Expected response type after processing the request.
 * @author Nikola Velemir
 * @version ${project.version}
 * @since 1.0.0
 */
@FunctionalInterface
public interface RequestInvocationChain<TRequest extends Request<TResponse>, TResponse> {
    /**
     * Executes the full request processing chain. Calls behaviors in specified order, ending with a handler.
     * @param request Request to be processed.
     * @return Response object, which represents the result of request processing.
     */
    TResponse execute(TRequest request);
}