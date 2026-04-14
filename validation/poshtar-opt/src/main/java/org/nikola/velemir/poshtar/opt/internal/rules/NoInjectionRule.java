package org.nikola.velemir.poshtar.opt.internal.rules;

import org.nikola.velemir.poshtar.core.mediator.Poshtar;
import org.nikola.velemir.poshtar.opt.processor.ProcessorContext;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.*;
import javax.lang.model.type.TypeMirror;
import java.util.Set;

abstract class NoInjectionRule implements Rule {
    protected static final String MEDIATOR_FQN = Poshtar.class.getName();


    @Override
    public void validate(RoundEnvironment roundEnv, ProcessorContext ctx) {
        Set<String> forbidden = ctx.getAll();
        if (forbidden.isEmpty()) return;

        for (Element root : roundEnv.getRootElements()) {
            if (root.getKind() != ElementKind.CLASS) continue;
            TypeElement clazz = (TypeElement) root;
            if (clazz.getQualifiedName().contentEquals(MEDIATOR_FQN)) continue;

            checkClassBody((TypeElement) root, forbidden, ctx);
        }
    }

    protected void checkClassBody(TypeElement clazz, Set<String> forbidden, ProcessorContext ctx) {
        for (Element enclosed : clazz.getEnclosedElements()) {

            validateFieldInjection(forbidden, ctx, enclosed);

            validateConstructorInjection(forbidden, ctx, enclosed);
            validateMethodInjection(forbidden, ctx, enclosed);
        }
    }

    protected void validateMethodInjection(Set<String> forbidden, ProcessorContext ctx, Element enclosed) {
        if (enclosed.getKind() == ElementKind.METHOD) {
            ExecutableElement method = (ExecutableElement) enclosed;
            for (VariableElement param : method.getParameters()) {
                if (isForbiddenType(param.asType(), forbidden, ctx)) {
                    logError(param, ctx);
                }
            }
        }
    }

    protected void validateConstructorInjection(Set<String> forbidden, ProcessorContext ctx, Element enclosed) {
        if (enclosed.getKind() == ElementKind.CONSTRUCTOR) {
            ExecutableElement constructor = (ExecutableElement) enclosed;
            for (VariableElement param : constructor.getParameters()) {
                if (isForbiddenType(param.asType(), forbidden, ctx)) {
                    logError(param, ctx);
                }
            }
        }
    }

    protected abstract void logError(Element target, ProcessorContext ctx);

    protected void validateFieldInjection(Set<String> forbidden, ProcessorContext ctx, Element enclosed) {
        if (enclosed.getKind() == ElementKind.FIELD) {
            VariableElement field = (VariableElement) enclosed;
            if (isForbiddenType(field.asType(), forbidden, ctx)) {
                logError(field, ctx);
            }
        }
    }

    protected abstract boolean isForbiddenType(TypeMirror type, Set<String> forbidden, ProcessorContext ctx);
}
