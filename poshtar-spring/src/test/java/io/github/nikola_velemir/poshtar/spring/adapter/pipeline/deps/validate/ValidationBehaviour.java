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

package io.github.nikola_velemir.poshtar.spring.adapter.pipeline.deps.validate;


import io.github.nikola_velemir.poshtar.core.annotations.Behaviour;
import io.github.nikola_velemir.poshtar.core.pipeline.behaviour.PipelineBehaviour;
import io.github.nikola_velemir.poshtar.core.pipeline.delegate.RequestDelegate;

@Behaviour
public class ValidationBehaviour implements PipelineBehaviour<ValidationRequest, Integer> {
    @Override
    public Integer handle(ValidationRequest request, RequestDelegate<ValidationRequest, Integer> delegate) {
        System.out.println("Entered validation behaviour!");
        validate(request.payload());
        System.out.println("Passed validation!");
        return delegate.handle(request);
    }

    private void validate(int inputPayload) {
        if (inputPayload == 0) {
            throw new IllegalArgumentException("Payload is wrong");
        }
    }
}
