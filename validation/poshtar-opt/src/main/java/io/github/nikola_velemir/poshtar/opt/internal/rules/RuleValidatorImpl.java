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


import io.github.nikola_velemir.poshtar.opt.internal.context.ProcessorContext;

import javax.annotation.processing.RoundEnvironment;
import java.util.List;

/**
 * Base implementation of the {@link RuleValidator}.
 * <p>During validation, passes thru the list of rules provided by {@link RuleProvider}, calling validate on each one</p>
 *
 */
class RuleValidatorImpl implements RuleValidator {
    private final List<Rule> rules = RuleProvider.provideRules();

    public void validateRules(RoundEnvironment roundEnv, ProcessorContext ctx) {
        for (Rule rule : rules) {
            rule.validate(roundEnv, ctx);
        }
    }
}
