package org.nikola.velemir.poshtar.opt.processor;

import com.google.auto.service.AutoService;
import com.sun.source.util.Trees;
import org.nikola.velemir.poshtar.core.annotations.Behaviour;
import org.nikola.velemir.poshtar.core.annotations.Handler;
import org.nikola.velemir.poshtar.core.request.handler.RequestHandler;
import org.nikola.velemir.poshtar.opt.processor.utils.IdeUnwrapper;
import org.nikola.velemir.poshtar.opt.processor.utils.RegistryManager;
import org.nikola.velemir.poshtar.opt.processor.utils.RuleValidator;
import org.nikola.velemir.poshtar.opt.rules.ambiguity.AmbiguityRule;
import org.nikola.velemir.poshtar.opt.rules.deadPipeline.DeadPipelineRule;
import org.nikola.velemir.poshtar.opt.rules.injection.BehaviourNoInjectionRule;
import org.nikola.velemir.poshtar.opt.rules.injection.HandlerNoInjectionRule;
import org.nikola.velemir.poshtar.opt.rules.Rule;
import org.nikola.velemir.poshtar.opt.rules.RuleContext;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
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
            if (this.registry != null) RegistryManager.writeRegistry(processingEnv, this.registry);
            return false;
        }

        if (registry == null) registry = RegistryManager.loadExistingRegistry(processingEnv);
        RuleContext ctx = new RuleContext(processingEnv, trees, registry);

        RegistryManager.preprocessRegistry(roundEnv, ctx);

        validator.validateRules(roundEnv, ctx);

        return false;
    }


    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        ProcessingEnvironment unwrapped = IdeUnwrapper.jbUnwrap(ProcessingEnvironment.class, processingEnv);
        this.trees = Trees.instance(unwrapped);
    }

}
