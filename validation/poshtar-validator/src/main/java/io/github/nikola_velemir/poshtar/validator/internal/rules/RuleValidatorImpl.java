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


import io.github.nikola_velemir.poshtar.validator.internal.context.ProcessorContext;
import io.github.nikola_velemir.poshtar.validator.internal.rules.deadPipeline.DeadPipelineRuleProvider;
import io.github.nikola_velemir.poshtar.validator.internal.rules.finality.FinalityRuleProvider;
import io.github.nikola_velemir.poshtar.validator.internal.rules.noInjection.NoInjectionRuleProvider;
import io.github.nikola_velemir.poshtar.validator.internal.rules.registration.RegistrationRuleProvider;
import io.github.nikola_velemir.poshtar.validator.internal.rules.responsibility.ResponsibilityRuleProvider;
import io.github.nikola_velemir.poshtar.validator.internal.rules.returnTypes.ReturnTypesRuleProvider;
import io.github.nikola_velemir.poshtar.validator.internal.rules.wiring.WiringRuleProvider;

import javax.annotation.processing.RoundEnvironment;
import java.util.List;
import java.util.stream.Stream;

/**
 * Base implementation of the {@link RuleValidator}.
 * <p>During validation, passes thru the list of rules provided by rule providers,
 * calling validate on each one</p>
 *
 */
class RuleValidatorImpl implements RuleValidator {
    private static final List<Rule> RULES = provideRules();

    private static List<Rule> provideRules(){
       return Stream.of(
                DeadPipelineRuleProvider.provide(),
                FinalityRuleProvider.provide(),
                NoInjectionRuleProvider.provide(),
                RegistrationRuleProvider.provide(),
                ResponsibilityRuleProvider.provide(),
                ReturnTypesRuleProvider.provide(),
                WiringRuleProvider.provide()
        ).flatMap(List::stream
        ).toList();
    }

    public void validateRules(RoundEnvironment roundEnv, ProcessorContext ctx) {
        for (Rule rule : RULES) {
            rule.validate(roundEnv, ctx);
        }
    }
}
