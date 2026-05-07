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

package io.github.nikola_velemir.poshtar.opt.internal.rules;

/**
 * Class acts as an injector to provide {@link RuleValidator} implementation, since implementation is package private.
 */
public class RuleValidatorProvider {
    /**
     * Provides the implementation of {@link RuleValidator}, as the concrete implementation is package-private.
     *
     * @return Implementation of the {@link RuleValidator}, which will be used by the processor.
     */
    public static RuleValidator provideValidator() {
        return new RuleValidatorImpl();
    }
}
