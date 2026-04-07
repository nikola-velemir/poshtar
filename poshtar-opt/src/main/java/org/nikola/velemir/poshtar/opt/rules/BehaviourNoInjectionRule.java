package org.nikola.velemir.poshtar.opt.rules;

import org.nikola.velemir.poshtar.core.pipeline.behaviour.PipelineBehaviour;
import org.nikola.velemir.poshtar.opt.processor.utils.ErrorLogger;

import javax.lang.model.element.*;
import javax.lang.model.type.TypeMirror;
import java.util.Set;

 class BehaviourNoInjectionRule extends NoInjectionRule {

    private static final String BEHAVIOUR_INTERFACE_FQN = PipelineBehaviour.class.getName();

    @Override
    protected boolean isForbiddenType(TypeMirror type, Set<String> forbidden, RuleContext ctx) {
        var typeUtils = ctx.env.getTypeUtils();
        var elementUtils = ctx.env.getElementUtils();

        TypeMirror behaviour = elementUtils.getTypeElement(BEHAVIOUR_INTERFACE_FQN).asType();

        TypeMirror erasedType = typeUtils.erasure(type);
        TypeMirror erasedBehaviour = typeUtils.erasure(behaviour);

        return typeUtils.isAssignable(erasedType, erasedBehaviour);
    }

    @Override
    protected void logError(Element target, RuleContext ctx) {
        String errorMessage =
                "PoshtaR VIOLATION: Behaviours cannot be injected, set thru methods or constructor, or manually managed. " +
                        "Use 'Poshtar.send(request)' to interact with this logic.";
        ErrorLogger.logError(ctx.env, errorMessage, target);

    }
}