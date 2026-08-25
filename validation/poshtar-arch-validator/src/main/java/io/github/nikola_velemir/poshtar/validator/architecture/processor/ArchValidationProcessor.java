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

package io.github.nikola_velemir.poshtar.validator.architecture.processor;

import com.google.auto.service.AutoService;
import com.sun.source.util.Trees;
import io.github.nikola_velemir.poshtar.core.annotations.Behaviour;
import io.github.nikola_velemir.poshtar.core.annotations.Handler;
import io.github.nikola_velemir.poshtar.validator.base.internal.rules.RuleValidatorProvider;
import io.github.nikola_velemir.poshtar.validator.base.processor.AbstractValidationProcessor;
import io.github.nikola_velemir.poshtar.validator.architecture.internal.rules.ValidatorRuleProvider;

import javax.annotation.processing.*;

/**
 * Annotation processor that performs compile-time validation of Poshtar components
 *
 * <p>
 * This processor scans for classes annotated with {@link Handler} or {@link Behaviour}
 * and applies a set of architectural rules to ensure the mediator logic is
 * configured correctly. By catching errors during compilation, it prevents
 * runtime failures.
 * </p>
 *
 * <p>The processor performs the following checks:</p>
 * <ul>
 *     <li><b>Registry Scanning:</b> Builds a map of requests to handlers to detect missing or duplicate definitions.</li>
 *     <li><b>Rule Validation:</b> Verifies that handlers implement the correct interfaces and that behaviors follow the required contract.</li>
 *     <li><b>IDE Integration:</b> Uses {@link Trees} to provide precise error highlighting directly on the source code.</li>
 * </ul>
 *
 * @author Nikola Velemir
 * @version ${project.version}
 * @since 1.0.0
 */
@AutoService(Processor.class)
public class ArchValidationProcessor extends AbstractValidationProcessor {

    @Override
    protected RuleValidatorProvider provideRuleValidatorProvider() {
        return new ValidatorRuleProvider();
    }


}
