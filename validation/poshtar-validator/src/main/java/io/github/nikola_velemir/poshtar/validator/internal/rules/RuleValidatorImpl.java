/*
 * Copyright (C) 2026 Nikola (nvelem.nikola@gmail.com)
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
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
