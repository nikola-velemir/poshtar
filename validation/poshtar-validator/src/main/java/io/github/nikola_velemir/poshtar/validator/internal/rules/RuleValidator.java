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

import javax.annotation.processing.RoundEnvironment;
import java.util.List;

/**
 * Interface defines a validator of the architectural rules, specified by the library.
 */
public interface RuleValidator {
    /**
     * Method validates all rules provided to the implementation of this interface.
     *
     * @param roundEnv The environment providing access to elements in the current processing round.
     * @param ctx      Instance of context containing all.
     */
    void validateRules(RoundEnvironment roundEnv, ProcessorContext ctx);

    static List<Rule> provideRules() {
        return List.of();
    }
}