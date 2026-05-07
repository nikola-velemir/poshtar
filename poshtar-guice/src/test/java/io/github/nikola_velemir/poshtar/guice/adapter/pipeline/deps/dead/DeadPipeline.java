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

package io.github.nikola_velemir.poshtar.guice.adapter.pipeline.deps.dead;

import io.github.nikola_velemir.poshtar.core.annotations.Behaviour;
import io.github.nikola_velemir.poshtar.core.pipeline.behaviour.PipelineBehaviour;
import io.github.nikola_velemir.poshtar.core.pipeline.delegate.RequestDelegate;
import io.github.nikola_velemir.poshtar.core.types.Unit;
import io.github.nikola_velemir.poshtar.opt.api.annotations.pipeline.SuppressDead;

@SuppressDead
@Behaviour
public class DeadPipeline implements PipelineBehaviour<DeadRequest, Unit> {
    @Override
    public Unit handle(DeadRequest request, RequestDelegate<DeadRequest, Unit> requestDelegate) {
        //handleDead(request, requestDelegate);
        return null;
    }

    private static Unit handleDead(DeadRequest request, RequestDelegate<DeadRequest, Unit> requestDelegate) {
        System.out.println("Called dead pipeline!");
        return requestDelegate.handle(request);
    }
}
