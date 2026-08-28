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
import io.github.nikola_velemir.poshtar.validator.internal.rules.architectural.ArchitecturalRuleProvider;
import io.github.nikola_velemir.poshtar.validator.internal.rules.semantical.SemanticalRuleProvider;

import javax.annotation.processing.RoundEnvironment;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

/**
 * Base implementation of the {@link RuleValidator}.
 * <p>During validation, passes thru the list of rules provided by rule providers,
 * calling validate on each one</p>
 *
 */
class RuleValidatorImpl implements RuleValidator {
    private final List<Rule> rules;

    RuleValidatorImpl(Set<RuleKind> kinds) {
        this.rules = provideRules(kinds);
    }

    private List<Rule> provideRules(Set<RuleKind> enabledKinds) {
        return Stream.of(
                new ArchitecturalRuleProvider(),
                new SemanticalRuleProvider())
                .filter(p -> enabledKinds.contains(p.getKind())).flatMap(p -> p.provide().stream()).toList();
    }

    public void validateRules(RoundEnvironment roundEnv, ProcessorContext ctx) {
        for (Rule rule : rules) {
            rule.validate(roundEnv, ctx);
        }
    }
}
