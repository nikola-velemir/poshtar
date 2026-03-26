package org.nikola.velemir.poshtar.opt.rules.injection;

import org.nikola.velemir.poshtar.core.mediator.Poshtar;
import org.nikola.velemir.poshtar.opt.rules.Rule;
import org.nikola.velemir.poshtar.opt.rules.RuleContext;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.*;
import javax.lang.model.type.TypeMirror;
import java.util.Set;

public abstract class NoInjectionRule implements Rule {
    protected static final String MEDIATOR_FQN = Poshtar.class.getName();

    @Override
    public void validate(TypeElement element, RuleContext ctx) {

    }

    @Override
    public void validateRound(RoundEnvironment roundEnv, RuleContext ctx) {

    }

    protected void checkClassBody(TypeElement clazz, Set<String> forbidden, RuleContext ctx) {
        for (Element enclosed : clazz.getEnclosedElements()) {

            validateFieldInjection(forbidden, ctx, enclosed);

            validateConstructorInjection(forbidden, ctx, enclosed);
            validateMethodInjection(forbidden, ctx, enclosed);
        }
    }

    public void validateMethodInjection(Set<String> forbidden, RuleContext ctx, Element enclosed) {
        if (enclosed.getKind() == ElementKind.METHOD) {
            ExecutableElement method = (ExecutableElement) enclosed;
            for (VariableElement param : method.getParameters()) {
                if (isForbiddenType(param.asType(), forbidden, ctx)) {
                    logViolation(param, ctx);
                }
            }
        }
    }

    protected void validateConstructorInjection(Set<String> forbidden, RuleContext ctx, Element enclosed) {
        if (enclosed.getKind() == ElementKind.CONSTRUCTOR) {
            ExecutableElement constructor = (ExecutableElement) enclosed;
            for (VariableElement param : constructor.getParameters()) {
                if (isForbiddenType(param.asType(), forbidden, ctx)) {
                    logViolation(param, ctx);
                }
            }
        }
    }
    protected void logViolation(Element target, RuleContext ctx) {
    }
    protected void validateFieldInjection(Set<String> forbidden, RuleContext ctx, Element enclosed) {
        if (enclosed.getKind() == ElementKind.FIELD) {
            VariableElement field = (VariableElement) enclosed;
            if (isForbiddenType(field.asType(), forbidden, ctx)) {
                logViolation(field, ctx);
            }
        }
    }

    protected boolean isForbiddenType(TypeMirror type, Set<String> forbidden, RuleContext ctx) {
        return true;
    }
}
