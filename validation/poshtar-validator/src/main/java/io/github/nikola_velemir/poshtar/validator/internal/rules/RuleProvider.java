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

package io.github.nikola_velemir.poshtar.validator.internal.rules;


import java.util.List;

/**
 * Class that provides all architectural rules to be checked during compilation.
 *
 * <p>Class is acts as "injector", providing rules to validator classes</p>
 */
class RuleProvider {
    private static final List<Rule> rules = List.of(
            new BehaviourWiringRule(),
            new HandlerWiringRule(),
            new SingleResponsibilityHandlerRule(),
            new NoPrimitiveReturnTypesRule(),
            new OrphanRequestRule(),
            new RequestFinalityRule(),
            new NotificationFinalityRule(),
            new AmbiguityRule(),
            new HandlerNoInjectionRule(),
            new BehaviourNoInjectionRule(),
            new DeadPipelineRule()
    );

    /**
     * Provides all architectural rules to validate.
     *
     * @return List of rules to be validated.
     */
    public static List<Rule> provideRules() {
        return rules;
    }
}
