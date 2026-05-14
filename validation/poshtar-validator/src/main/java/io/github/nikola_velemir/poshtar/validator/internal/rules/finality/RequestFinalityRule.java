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

package io.github.nikola_velemir.poshtar.validator.internal.rules.finality;

import io.github.nikola_velemir.poshtar.validator.internal.context.ProcessorContext;

import java.util.Set;

/**
 * Rule that prevents the developer from creating request class inheritance.
 *
 * <p>
 * Rule disallows the inheritance between classes that extend
 * {@link io.github.nikola_velemir.poshtar.core.request.Request},
 * by forcing such classes to be records or declared final.
 * </p>
 *
 * @author Nikola Velemir
 * @version ${project.version}
 * @since 1.0.0
 */
class RequestFinalityRule extends FinalityRule {

    @Override
    protected String getViolationMessage() {
        String FINALITY_VIOLATED_MESSAGE = "PoshtaR: Finality Violated! Request '%s' must be final or a record!";
        return FINALITY_VIOLATED_MESSAGE;
    }

    @Override
    protected Set<String> getFQNs(ProcessorContext ctx) {
        return ctx.getKnownRequests();
    }
}