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

package io.github.nikola_velemir.poshtar.validator.processor;

import com.google.auto.service.AutoService;
import com.sun.source.util.Trees;
import io.github.nikola_velemir.poshtar.core.annotations.Behaviour;
import io.github.nikola_velemir.poshtar.core.annotations.Handler;
import io.github.nikola_velemir.poshtar.validator.internal.context.ProcessorContext;
import io.github.nikola_velemir.poshtar.validator.internal.options.OptionsResolver;
import io.github.nikola_velemir.poshtar.validator.internal.registry.scanner.RegistryScanner;

import io.github.nikola_velemir.poshtar.validator.internal.registry.scanner.RegistryScannerProvider;
import io.github.nikola_velemir.poshtar.validator.internal.rules.RuleKind;
import io.github.nikola_velemir.poshtar.validator.internal.rules.RuleValidator;
import io.github.nikola_velemir.poshtar.validator.internal.rules.RuleValidatorProvider;
import io.github.nikola_velemir.poshtar.validator.internal.unwrapper.IdeUnwrapper;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
public class PoshtarValidationProcessor extends AbstractProcessor {
    /**
     * The set of annotations this processor is interested in monitoring.
     */


    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    private Set<RuleKind> enabledRuleKinds;
    private Trees trees;
    private RuleValidator validator;
    private final RegistryScanner scanner = RegistryScannerProvider.provideScanner();

    /**
     * Processes a round of annotation discovery.
     * <p>
     * This method initializes the {@link ProcessorContext}, scans the current
     * compilation round for Poshtar components, and runs the validation ruleset.
     * </p>
     *
     * @param annotations The annotation types requested to be processed.
     * @param roundEnv    The environment for information about the current and prior rounds.
     * @return {@code false}, allowing other processors to see these annotations.
     */
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

        ProcessorContext ctx = new ProcessorContext(processingEnv, trees);
        scanner.scanRegistry(roundEnv, ctx);

        validator.validateRules(roundEnv, ctx);

        return false;
    }

    /**
     * Returns the names of the annotation types supported by this processor.
     *
     * @return A set of fully qualified annotation names.
     */
    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Set.of(ProcessorConstants.ANNOTATIONS);
    }

    /**
     * Initializes the processor with the processing environment.
     * <p>
     * This implementation includes a specialized "unwrapping" step to ensure
     * compatibility with IDE-specific compilers (like IntelliJ or Eclipse)
     * when accessing the {@link Trees} API.
     * </p>
     *
     * @param processingEnv The environment provided by the tool framework.
     */
    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);

        String ruleOptionName = ProcessorConstants.OPTIONS.get(ProcessorConstants.OptionKey.RULES);
        enabledRuleKinds = OptionsResolver.resolveEnabledRuleKinds(ruleOptionName, processingEnv);
        validator = RuleValidatorProvider.provideValidator(enabledRuleKinds);
        ProcessingEnvironment unwrapped = IdeUnwrapper.unwrap(ProcessingEnvironment.class, processingEnv);
        this.trees = Trees.instance(unwrapped);
    }

    @Override
    public Set<String> getSupportedOptions() {
        Set<String> options = new HashSet<>(super.getSupportedOptions());
        Collections.addAll(options, ProcessorConstants.OPTIONS.values().toArray(String[]::new));
        return options;
    }
}
