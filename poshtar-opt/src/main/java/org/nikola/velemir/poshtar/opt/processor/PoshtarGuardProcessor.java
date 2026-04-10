package org.nikola.velemir.poshtar.opt.processor;

import com.google.auto.service.AutoService;
import com.sun.source.util.Trees;
import org.nikola.velemir.poshtar.opt.internal.registry.RegistryStore;
import org.nikola.velemir.poshtar.opt.internal.unwrapper.IdeUnwrapper;
import org.nikola.velemir.poshtar.opt.internal.registry.RegistryProcessor;
import org.nikola.velemir.poshtar.opt.internal.rules.RuleValidator;
import org.nikola.velemir.poshtar.opt.internal.rules.RuleContext;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import java.util.Properties;
import java.util.Set;

@AutoService(Processor.class)
@SupportedAnnotationTypes("*")
@SupportedSourceVersion(SourceVersion.RELEASE_21)
public class PoshtarGuardProcessor extends AbstractProcessor {


    private Properties registry;
    private Trees trees;
    private final RuleValidator validator = new RuleValidator();

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (roundEnv.processingOver()) {
            if (this.registry != null) RegistryStore.writeRegistry(processingEnv, this.registry);
            return false;
        }

        if (registry == null) registry = RegistryStore.loadExistingRegistry(processingEnv);
        RuleContext ctx = new RuleContext(processingEnv, trees);

        RegistryProcessor.preprocessRegistry(roundEnv, ctx);

        validator.validateRules(roundEnv, ctx);

        return false;
    }


    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        ProcessingEnvironment unwrapped = IdeUnwrapper.unwrap(ProcessingEnvironment.class, processingEnv);
        this.trees = Trees.instance(unwrapped);
    }

}
