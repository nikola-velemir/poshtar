package io.github.nikola_velemir.poshtar.opt.processor;

import com.google.auto.service.AutoService;
import com.sun.source.util.Trees;
import io.github.nikola_velemir.poshtar.core.annotations.Behaviour;
import io.github.nikola_velemir.poshtar.core.annotations.Handler;
import io.github.nikola_velemir.poshtar.opt.internal.context.ProcessorContext;
import io.github.nikola_velemir.poshtar.opt.internal.registry.scanner.RegistryScanner;

import io.github.nikola_velemir.poshtar.opt.internal.registry.scanner.RegistryScannerProvider;
import io.github.nikola_velemir.poshtar.opt.internal.rules.RuleValidator;
import io.github.nikola_velemir.poshtar.opt.internal.rules.RuleValidatorProvider;
import io.github.nikola_velemir.poshtar.opt.internal.unwrapper.IdeUnwrapper;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import java.util.Set;


@AutoService(Processor.class)
@SupportedSourceVersion(SourceVersion.RELEASE_21)
public class PoshtarValidationProcessor extends AbstractProcessor {
    private static final String[] ANNOTATIONS = {
            Handler.class.getName(),
            Behaviour.class.getName()
    };

    private Trees trees;
    private final RuleValidator validator = RuleValidatorProvider.provideValidator();
    private final RegistryScanner scanner = RegistryScannerProvider.provideScanner();

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

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Set.of(ANNOTATIONS);
    }

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        ProcessingEnvironment unwrapped = IdeUnwrapper.unwrap(ProcessingEnvironment.class, processingEnv);
        this.trees = Trees.instance(unwrapped);
    }

}
