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

package io.github.nikola_velemir.poshtar.validator.processor;

import com.google.auto.service.AutoService;
import com.sun.source.util.Trees;
import io.github.nikola_velemir.poshtar.core.annotations.Behaviour;
import io.github.nikola_velemir.poshtar.core.annotations.Handler;
import io.github.nikola_velemir.poshtar.validator.internal.context.ProcessorContext;
import io.github.nikola_velemir.poshtar.validator.internal.registry.scanner.RegistryScanner;

import io.github.nikola_velemir.poshtar.validator.internal.registry.scanner.RegistryScannerProvider;
import io.github.nikola_velemir.poshtar.validator.internal.rules.RuleValidator;
import io.github.nikola_velemir.poshtar.validator.internal.rules.RuleValidatorProvider;
import io.github.nikola_velemir.poshtar.validator.internal.unwrapper.IdeUnwrapper;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
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
@SupportedSourceVersion(SourceVersion.RELEASE_21)
public class PoshtarValidationProcessor extends AbstractProcessor {
    /**
     * The set of annotations this processor is interested in monitoring.
     */
    private static final String[] ANNOTATIONS = {
            Handler.class.getName(),
            Behaviour.class.getName()
    };

    private Trees trees;
    private final RuleValidator validator = RuleValidatorProvider.provideValidator();
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
        if (roundEnv.processingOver()) {
            return false;
        }


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
        return Set.of(ANNOTATIONS);
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
        ProcessingEnvironment unwrapped = IdeUnwrapper.unwrap(ProcessingEnvironment.class, processingEnv);
        this.trees = Trees.instance(unwrapped);
    }

}
